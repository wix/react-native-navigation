#import "RNNBottomTabsController.h"
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

    [self resolveCustomItemViewMode:childViewControllers];

    if (@available(iOS 13.0, *)) {
        // The opaque-white standardAppearance below was added long ago for an
        // iOS 13/15 background-color issue. On iOS 26 it overrides the new
        // floating-glass platter look. Skip it when custom item views are
        // active so iOS 26 renders its native floating tab bar.
        if (!_useCustomItemViews) {
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
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    // iOS 26: first layout can misplace tab item titles; cycling selection (then restoring) forces a
    // correct layout without user interaction. Defer so all tab children are in the hierarchy.
    if (@available(iOS 26.0, *)) {
        if (!_rnnDidApplyInitialTabBarSelectionFix) {
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
    }

    [self syncTabBarItemTestIDs];
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
}

// Walks `tabBar`'s view tree and returns the views that look like tab buttons.
// On iOS <26 these are direct `UITabBarButton` subviews of `UITabBar`. On
// iOS 26 the floating platter wraps a layout container that holds private
// `_UITabButton` views. We don't depend on the exact class hierarchy: we
// match by class name suffix and return only views that are visible (non-zero
// frame, not hidden, alpha > 0).
- (NSArray<UIView *> *)findTabBarButtonViews {
    NSMutableArray<UIView *> *result = [NSMutableArray array];
    [self collectTabBarButtonViewsInView:self.tabBar into:result];

    [result filterUsingPredicate:[NSPredicate predicateWithBlock:^BOOL(UIView *view, NSDictionary *_) {
        if (view.hidden || view.alpha < 0.01) {
            return NO;
        }
        return view.bounds.size.width > 0 && view.bounds.size.height > 0;
    }]];

    UITabBarController *strongSelf = self;
    [result sortUsingComparator:^NSComparisonResult(UIView *a, UIView *b) {
        CGRect frameA = [a.superview convertRect:a.frame toView:strongSelf.view];
        CGRect frameB = [b.superview convertRect:b.frame toView:strongSelf.view];
        if (frameA.origin.x < frameB.origin.x) return NSOrderedAscending;
        if (frameA.origin.x > frameB.origin.x) return NSOrderedDescending;
        return NSOrderedSame;
    }];

    return result;
}

- (void)collectTabBarButtonViewsInView:(UIView *)view
                                  into:(NSMutableArray<UIView *> *)result {
    NSString *className = NSStringFromClass([view class]);
    // `UITabBarButton` (legacy) and `_UITabButton` (iOS 26) are the leaf tap
    // targets we care about.
    if ([className isEqualToString:@"UITabBarButton"] ||
        [className isEqualToString:@"_UITabButton"]) {
        // iOS 26 renders TWO `_UITabButton`s per tab: one inside the
        // platter (`_UITabBarPlatterView.ContentView`) — that's the real
        // interactive button — and one inside the selection-content view
        // (`_UITabBarVisualProvider_Floating.SelectedContentView`) used to
        // animate the highlighted pill background. Skip the latter so we
        // don't attach our React view to a non-interactive duplicate.
        UIView *ancestor = view.superview;
        while (ancestor) {
            NSString *aName = NSStringFromClass([ancestor class]);
            if ([aName containsString:@"SelectedContentView"]) {
                return;
            }
            ancestor = ancestor.superview;
        }
        [result addObject:view];
        return; // Don't descend — these are leaf controls.
    }
    for (UIView *subview in view.subviews) {
        [self collectTabBarButtonViewsInView:subview into:result];
    }
}

// Attaches each `RNNCustomTabBarItemView` as a subview of the corresponding
// native tab button, frame-matched to the button's bounds. The native bar
// keeps drawing its background (legacy chrome / iOS 26 floating glass) and
// the system selected pill — our React view renders inside the slot.
//
// Native taps reach the underlying button because our React view stack has
// `userInteractionEnabled = NO`, so hit-testing falls through to the button.
// UIKit then triggers `tabBarController:didSelectViewController:` and
// `setSelectedViewController:`, which already update our selection state.
- (void)attachCustomItemViewsToButtons {
    if (!_useCustomItemViews || _customTabItemViews.count == 0) {
        return;
    }

    NSArray<UIView *> *buttons = [self findTabBarButtonViews];
    NSLog(@"[RNN.CustomTab] attach attempt buttons=%lu items=%lu tabBar.bounds=%@ tabBar.safeBottom=%.1f",
          (unsigned long)buttons.count,
          (unsigned long)_customTabItemViews.count,
          NSStringFromCGRect(self.tabBar.bounds),
          self.tabBar.safeAreaInsets.bottom);
    for (NSUInteger i = 0; i < buttons.count; i++) {
        UIView *b = buttons[i];
        CGRect inSelf = [b.superview convertRect:b.frame toView:self.view];
        NSLog(@"[RNN.CustomTab] candidate %lu cls=%@ parent=%@ frame=%@ inSelf=%@ hidden=%d alpha=%.2f",
              (unsigned long)i,
              NSStringFromClass([b class]),
              NSStringFromClass([b.superview class]),
              NSStringFromCGRect(b.frame),
              NSStringFromCGRect(inSelf),
              b.hidden,
              b.alpha);
    }
    NSUInteger count = MIN(buttons.count, _customTabItemViews.count);
    if (count == 0) {
        return;
    }

    CGFloat safeBottom = self.tabBar.safeAreaInsets.bottom;

    for (NSUInteger i = 0; i < count; i++) {
        UIView *button = buttons[i];
        RNNCustomTabBarItemView *itemView = _customTabItemViews[i];
        if (itemView.superview != button) {
            [itemView removeFromSuperview];
            [button addSubview:itemView];
        }
        CGRect contentFrame = button.bounds;
        if (safeBottom > 0 && contentFrame.size.height > safeBottom + 24) {
            contentFrame.size.height -= safeBottom;
        }
        itemView.frame = contentFrame;
        itemView.autoresizingMask = UIViewAutoresizingFlexibleWidth;
        [button bringSubviewToFront:itemView];
    }
}

- (void)updateCustomTabItemSelection {
    if (!_useCustomItemViews) {
        return;
    }
    for (NSUInteger i = 0; i < _customTabItemViews.count; i++) {
        [_customTabItemViews[i] setSelected:(i == _currentTabIndex)];
    }
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
    [self attachCustomItemViewsToButtons];
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
