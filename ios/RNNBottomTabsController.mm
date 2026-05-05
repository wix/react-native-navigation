#import "RNNBottomTabsController.h"
#import "UITabBarController+RNNOptions.h"
#import "UITabBarController+RNNUtils.h"

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
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                bottomTabPresenter:(BottomTabPresenter *)bottomTabPresenter
             dotIndicatorPresenter:(RNNDotIndicatorPresenter *)dotIndicatorPresenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter
              childViewControllers:(NSArray *)childViewControllers
                bottomTabsAttacher:(BottomTabsBaseAttacher *)bottomTabsAttacher {
    _bottomTabsAttacher = bottomTabsAttacher;
    _bottomTabPresenter = bottomTabPresenter;
    _dotIndicatorPresenter = dotIndicatorPresenter;
    _options = options;
    _didFinishSetup = NO;

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
    for (UIViewController *child in childViewControllers) {
        [_bottomTabPresenter applyOptions:child.resolveOptions child:child];
    }

    [self syncTabBarItemTestIDs];
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
}

- (UIViewController *)selectedViewController {
    return self.childViewControllers.count ? self.childViewControllers[_currentTabIndex] : nil;
}

- (void)setSelectedViewController:(__kindof UIViewController *)selectedViewController {
    _previousTabIndex = _currentTabIndex;
    _currentTabIndex = [self.childViewControllers indexOfObject:selectedViewController];
    [super setSelectedViewController:selectedViewController];
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

@end
