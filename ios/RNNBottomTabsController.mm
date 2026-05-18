#import "RNNBottomTabsController.h"
#import "RNNBottomTabsCustomRow.h"
#import "RNNCustomTabBarItemView.h"
#import "RNNTabBarItemCreator.h"
#import "UITabBarController+RNNOptions.h"
#import "UITabBarController+RNNUtils.h"
#import <React/RCTLog.h>

@interface RNNBottomTabsController ()
@property(nonatomic, strong) BottomTabPresenter *bottomTabPresenter;
@property(nonatomic, strong) RNNDotIndicatorPresenter *dotIndicatorPresenter;
@property(nonatomic, strong) UILongPressGestureRecognizer *longPressRecognizer;

- (void)rnn_cycleAllTabsThenRestoreInitialSelection;

@end

@implementation RNNBottomTabsController {
    NSUInteger _currentTabIndex;
    NSUInteger _previousTabIndex;
    BottomTabsBaseAttacher *_bottomTabsAttacher;
    BOOL _tabBarNeedsRestore;
    RNNNavigationOptions *_options;
    BOOL _didFinishSetup;
    BOOL _rnnDidApplyInitialTabBarSelectionFix;
    BOOL _rnnSuppressTabSelectionEvents;
    RNNReactComponentRegistry *_componentRegistry;
    NSMutableArray<RNNCustomTabBarItemView *> *_customTabItemViews;
    BOOL _useCustomItemViews;
    RNNBottomTabsCustomRow *_customRow;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                bottomTabPresenter:(BottomTabPresenter *)bottomTabPresenter
             dotIndicatorPresenter:(RNNDotIndicatorPresenter *)dotIndicatorPresenter
                 componentRegistry:(RNNReactComponentRegistry *)componentRegistry
                      eventEmitter:(RNNEventEmitter *)eventEmitter
              childViewControllers:(NSArray *)childViewControllers
                bottomTabsAttacher:(BottomTabsBaseAttacher *)bottomTabsAttacher {
    _bottomTabsAttacher = bottomTabsAttacher;
    _bottomTabPresenter = bottomTabPresenter;
    _dotIndicatorPresenter = dotIndicatorPresenter;
    _componentRegistry = componentRegistry;
    _options = options;
    _didFinishSetup = NO;
    _customTabItemViews = [NSMutableArray new];
    _useCustomItemViews = NO;

    IntNumber *currentTabIndex = options.bottomTabs.currentTabIndex;
    if ([currentTabIndex hasValue]) {
      NSUInteger currentTabIndexValue = [currentTabIndex get];
      _previousTabIndex = currentTabIndexValue;
      _currentTabIndex = currentTabIndexValue;
    }

    self = [super initWithLayoutInfo:layoutInfo
                           creator:creator
                           options:options
                    defaultOptions:defaultOptions
                         presenter:presenter
                      eventEmitter:eventEmitter
              childViewControllers:childViewControllers];

    if (@available(iOS 13.0, *)) {
        UITabBarAppearance *appearance = [UITabBarAppearance new];
        [appearance configureWithOpaqueBackground];
        appearance.backgroundEffect = nil;
        appearance.backgroundColor = UIColor.systemBackgroundColor;
        self.tabBar.standardAppearance = appearance;
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 150000
        if (@available(iOS 15.0, *)) {
            self.tabBar.scrollEdgeAppearance = [appearance copy];
        }
#endif
        self.tabBar.translucent = NO;
    }

    [self createTabBarItems:childViewControllers];

    self.longPressRecognizer =
        [[UILongPressGestureRecognizer alloc] initWithTarget:self
                                                      action:@selector(handleLongPressGesture:)];
    [self.tabBar addGestureRecognizer:self.longPressRecognizer];

    _didFinishSetup = YES;
    return self;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    // This hack is needed for cases when the initialized state of the tabBar should be hidden
    UINavigationController *selectedChild = self.selectedViewController;
    if ([selectedChild isKindOfClass:UINavigationController.class] &&
        selectedChild.hidesBottomBarWhenPushed) {
        [selectedChild pushViewController: [UIViewController new] animated:NO];
        [selectedChild popViewControllerAnimated:NO];
    }

    if (_useCustomItemViews) {
        [self ensureCustomRowAttached];
        [self layoutCustomRow];
    }
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // iOS 26: first layout can misplace tab item titles; cycling selection (then restoring) forces a
    // correct layout without user interaction. Defer so all tab children are in the hierarchy.
    // Skipped when custom item views are active — the native tab bar visuals are hidden anyway.
    if (@available(iOS 26.0, *)) {
        if (!_useCustomItemViews && !_rnnDidApplyInitialTabBarSelectionFix) {
            _rnnDidApplyInitialTabBarSelectionFix = YES;
            __weak RNNBottomTabsController *weakSelf = self;
            dispatch_async(dispatch_get_main_queue(), ^{
                [weakSelf rnn_cycleAllTabsThenRestoreInitialSelection];
            });
        }
    }
}

- (void)rnn_cycleAllTabsThenRestoreInitialSelection {
    NSUInteger count = self.childViewControllers.count;
    if (count <= 1) {
        return;
    }

    NSUInteger initial = _currentTabIndex;
    if (initial >= count) {
        initial = 0;
    }

    _rnnSuppressTabSelectionEvents = YES;
    [UIView performWithoutAnimation:^{
        for (NSUInteger i = 0; i < count; i++) {
            [self setSelectedIndex:i];
        }
        [self setSelectedIndex:initial];
    }];
    _rnnSuppressTabSelectionEvents = NO;
}

- (void)createTabBarItems:(NSArray<UIViewController *> *)childViewControllers {
    _bottomTabPresenter.tabCreator.searchRoleUsed = NO;
    [self resolveCustomItemViewMode:childViewControllers];
    _bottomTabPresenter.useCustomItemViews = _useCustomItemViews;
    for (UIViewController *child in childViewControllers) {
        [_bottomTabPresenter applyOptions:child.resolveOptions child:child];
    }

    if (_useCustomItemViews) {
        [self buildCustomTabItemViews:childViewControllers];
        [self applyCustomItemViewsTabBarConfiguration];
        [self ensureCustomRowAttached];
    }

    [self syncTabBarItemTestIDs];
}

- (void)applyCustomItemViewsTabBarConfiguration {
    // Hide the native tab bar visuals so our custom row is the only thing
    // shown. The bar itself stays in the view hierarchy so that
    // `UITabBarController` keeps reserving the bottom safe-area inset for
    // the selected child controller and exposes the right frame for the
    // row to match.
    for (UIView *subview in self.tabBar.subviews) {
        subview.hidden = YES;
    }
    self.tabBar.tintColor = UIColor.clearColor;
    self.tabBar.unselectedItemTintColor = UIColor.clearColor;
    self.tabBar.backgroundColor = UIColor.clearColor;
}

- (void)resolveCustomItemViewMode:(NSArray<UIViewController *> *)childViewControllers {
    if (childViewControllers.count == 0) {
        _useCustomItemViews = NO;
        return;
    }

    NSUInteger withComponent = 0;
    for (UIViewController *child in childViewControllers) {
        RNNNavigationOptions *resolved = child.resolveOptions;
        if (resolved.bottomTab.component.name.hasValue) {
            withComponent++;
        }
    }

    if (withComponent == 0) {
        _useCustomItemViews = NO;
        return;
    }

    if (withComponent != childViewControllers.count) {
        RCTLogWarn(
            @"[RNN] Mixed bottomTab.component usage detected (%lu of %lu tabs). All tabs must "
            @"declare a component or none — falling back to native rendering for all tabs.",
            (unsigned long)withComponent, (unsigned long)childViewControllers.count);
        _useCustomItemViews = NO;
        return;
    }

    _useCustomItemViews = YES;
}

- (void)buildCustomTabItemViews:(NSArray<UIViewController *> *)childViewControllers {
    [self destroyCustomTabItemViews];

    NSString *parentComponentId = self.layoutInfo.componentId;
    for (NSUInteger i = 0; i < childViewControllers.count; i++) {
        UIViewController *child = childViewControllers[i];
        RNNNavigationOptions *resolved = child.resolveOptions;
        RNNComponentOptions *componentOptions = resolved.bottomTab.component;

        RNNReactView *reactView =
            [_componentRegistry createComponentIfNotExists:componentOptions
                                         parentComponentId:parentComponentId
                                             componentType:RNNComponentTypeBottomTabItem
                                       reactViewReadyBlock:nil];

        NSString *badge = [resolved.bottomTab.badge withDefault:nil];
        RNNCustomTabBarItemView *itemView =
            [[RNNCustomTabBarItemView alloc] initWithReactView:reactView
                                                      tabIndex:i
                                                      selected:(i == _currentTabIndex)
                                                         badge:badge];
        [_customTabItemViews addObject:itemView];
    }
}

- (void)destroyCustomTabItemViews {
    for (RNNCustomTabBarItemView *itemView in _customTabItemViews) {
        [itemView removeFromSuperview];
    }
    [_customTabItemViews removeAllObjects];
    [_customRow removeFromSuperview];
    _customRow = nil;
}

- (void)ensureCustomRowAttached {
    if (!_useCustomItemViews) {
        return;
    }
    if (!_customRow) {
        _customRow = [[RNNBottomTabsCustomRow alloc] initWithFrame:CGRectZero];
        __weak RNNBottomTabsController *weakSelf = self;
        _customRow.onTapAtIndex = ^(NSUInteger index) {
            [weakSelf handleCustomRowTapAtIndex:index];
        };
    }
    [_customRow setItemViews:_customTabItemViews];
    if (_customRow.superview != self.view) {
        [self.view addSubview:_customRow];
    } else {
        [self.view bringSubviewToFront:_customRow];
    }
}

- (void)layoutCustomRow {
    if (!_useCustomItemViews || !_customRow) {
        return;
    }
    CGRect tabBarFrame = self.tabBar.frame;
    if (CGRectIsEmpty(tabBarFrame)) {
        return;
    }
    CGRect rowFrame = [self.view convertRect:tabBarFrame fromView:self.tabBar.superview];

    // iOS 26 native floating tab bars are visibly taller than the legacy
    // 49pt bar. Extend the row upward (overlapping the screen content
    // behind the glass) to match that proportion and to give the React
    // cells room for an icon + label + a comfortable pill.
    if (@available(iOS 26.0, *)) {
        CGFloat extraHeight = 18.0;
        rowFrame.origin.y -= extraHeight;
        rowFrame.size.height += extraHeight;
    }

    _customRow.frame = rowFrame;
    _customRow.hidden = self.tabBar.hidden;
    [_customRow setSelectedIndex:_currentTabIndex];
}

- (void)handleCustomRowTapAtIndex:(NSUInteger)index {
    if (index >= self.childViewControllers.count) {
        return;
    }
    UIViewController *target = self.childViewControllers[index];
    [self.eventEmitter sendBottomTabPressed:@(index)];
    BOOL select = [[target resolveOptions].bottomTab.selectTabOnPress withDefault:YES];
    if (!select) {
        return;
    }
    NSUInteger previous = _currentTabIndex;
    [self setSelectedIndex:index];
    if (!_rnnSuppressTabSelectionEvents) {
        [self.eventEmitter sendBottomTabSelected:@(index) unselected:@(previous)];
    }
}

- (void)updateCustomTabItemSelection {
    if (!_useCustomItemViews) {
        return;
    }
    for (NSUInteger i = 0; i < _customTabItemViews.count; i++) {
        [_customTabItemViews[i] setSelected:(i == _currentTabIndex)];
    }
    [_customRow setSelectedIndex:_currentTabIndex];
}

- (void)mergeChildOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    [super mergeChildOptions:options child:child];
    UIViewController *childViewController = [self findViewController:child];
    [_bottomTabPresenter mergeOptions:options
                      resolvedOptions:childViewController.resolveOptions
                                child:childViewController];
    [_dotIndicatorPresenter mergeOptions:options
                         resolvedOptions:childViewController.resolveOptions
                                   child:childViewController];

    if (_useCustomItemViews && options.bottomTab.badge.hasValue) {
        NSUInteger index = [self.childViewControllers indexOfObject:childViewController];
        if (index != NSNotFound && index < _customTabItemViews.count) {
            [_customTabItemViews[index] setBadge:[options.bottomTab.badge withDefault:nil]];
        }
    }

    [self syncTabBarItemTestIDs];
}

- (id<UITabBarControllerDelegate>)delegate {
    return self;
}

- (void)render {
    [_bottomTabsAttacher attach:self];
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [self syncTabBarItemTestIDs];
    [self.presenter viewDidLayoutSubviews];
    [_dotIndicatorPresenter bottomTabsDidLayoutSubviews:self];
    if (_useCustomItemViews) {
        // Re-hide native subviews; UIKit recreates them on bounds changes.
        [self applyCustomItemViewsTabBarConfiguration];
        [self ensureCustomRowAttached];
        [self layoutCustomRow];
    }
}

- (UIViewController *)getCurrentChild {
    return self.selectedViewController;
}

- (CGFloat)getBottomTabsHeight {
    return self.tabBar.frame.size.height;
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
    NSArray *children = self.childViewControllers;
    for (id child in children) {
        UIViewController<RNNLayoutProtocol> *vc = child;

        if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] &&
            [vc.layoutInfo.componentId isEqualToString:componentID]) {
            NSUInteger selectedIndex = [children indexOfObject:child];
            [self setSelectedIndex:selectedIndex];
            _currentTabIndex = selectedIndex;
        }
    }
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
    IntNumber *currentTabIndex = _options.bottomTabs.currentTabIndex;
    if ([currentTabIndex hasValue] && !_didFinishSetup) {
        NSUInteger currentTabIndexValue = [currentTabIndex get];
        _currentTabIndex = currentTabIndexValue;
    } else {
        _currentTabIndex = selectedIndex;
    }

    [super setSelectedIndex:_currentTabIndex];
    [self updateCustomTabItemSelection];
}

- (UIViewController *)selectedViewController {
    return self.childViewControllers.count ? self.childViewControllers[_currentTabIndex] : nil;
}

- (void)setSelectedViewController:(__kindof UIViewController *)selectedViewController {
    _previousTabIndex = _currentTabIndex;
    _currentTabIndex = [self.childViewControllers indexOfObject:selectedViewController];
    [super setSelectedViewController:selectedViewController];
    [self updateCustomTabItemSelection];
}

- (void)setTabBarVisible:(BOOL)visible animated:(BOOL)animated {
    _tabBarNeedsRestore = YES;
    visible ? [self showTabBar:animated] : [self hideTabBar:animated];
}

- (void)setTabBarVisible:(BOOL)visible {
    if (_tabBarNeedsRestore || !self.presentedComponentViewController.navigationController) {
        [self setTabBarVisible:visible animated:NO];
        _tabBarNeedsRestore = NO;
    }
}

- (void)handleTabBarLongPress:(CGPoint)locationInTabBar {
    for (UITabBarItem *item in self.tabBar.items) {
        if (CGRectContainsPoint([[item valueForKey:@"view"] frame], locationInTabBar)) {
            NSUInteger tabIndex = [self.tabBar.items indexOfObject:item];
            [self.eventEmitter sendBottomTabLongPressed:@(tabIndex)];
            break;
        }
    }
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController
    didSelectViewController:(UIViewController *)viewController {
    if (_rnnSuppressTabSelectionEvents) {
        return;
    }
    [self.eventEmitter sendBottomTabSelected:@(tabBarController.selectedIndex)
                                  unselected:@(_previousTabIndex)];
}

- (void)handleLongPressGesture:(UILongPressGestureRecognizer *)recognizer {
    if (recognizer.state == UIGestureRecognizerStateBegan) {
        CGPoint locationInTabBar = [recognizer locationInView:self.tabBar];
        [self handleTabBarLongPress:locationInTabBar];
    }
}

- (BOOL)tabBarController:(UITabBarController *)tabBarController
    shouldSelectViewController:(UIViewController *)viewController {
    NSUInteger _index = [tabBarController.viewControllers indexOfObject:viewController];
    BOOL isMoreTab = ![tabBarController.viewControllers containsObject:viewController];

    if (_rnnSuppressTabSelectionEvents) {
        return YES;
    }

    [self.eventEmitter sendBottomTabPressed:@(_index)];

    if ([[viewController resolveOptions].bottomTab.selectTabOnPress withDefault:YES] || isMoreTab) {
        return YES;
    }

    return NO;
}

#pragma mark - UIViewController overrides

- (UIStatusBarStyle)preferredStatusBarStyle {
    return [self.presenter getStatusBarStyle];
}

- (BOOL)prefersStatusBarHidden {
    return [self.presenter getStatusBarVisibility];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.presenter getOrientation];
}

- (BOOL)hidesBottomBarWhenPushed {
    return [self.presenter hidesBottomBarWhenPushed];
}

- (void)dealloc {
    [self destroyCustomTabItemViews];
    if (_componentRegistry && self.layoutInfo.componentId) {
        [_componentRegistry clearComponentsForParentId:self.layoutInfo.componentId];
    }
}

@end
