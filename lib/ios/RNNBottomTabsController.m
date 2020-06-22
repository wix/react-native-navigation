#import "RNNBottomTabsController.h"
#import "UITabBarController+RNNUtils.h"

@interface RNNBottomTabsController ()
@property (nonatomic, strong) BottomTabPresenter* bottomTabPresenter;
@property (nonatomic, strong) RNNDotIndicatorPresenter* dotIndicatorPresenter;
@property (nonatomic) BOOL viewWillAppearOnce;
@property (nonatomic, strong) UILongPressGestureRecognizer *longPressRecognizer;

@end

@implementation RNNBottomTabsController {
	NSUInteger _currentTabIndex;
    NSUInteger _previousTabIndex;
    BottomTabsBaseAttacher* _bottomTabsAttacher;
    BOOL _tabBarNeedsRestore;
    
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
    _pendingChildViewControllers = childViewControllers;
    self = [super initWithLayoutInfo:layoutInfo creator:creator options:options defaultOptions:defaultOptions presenter:presenter eventEmitter:eventEmitter childViewControllers:childViewControllers];
    if (@available(iOS 13.0, *)) {
        self.tabBar.standardAppearance = [UITabBarAppearance new];
    }
    
    self.longPressRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(handleLongPressGesture:)];
    [self.tabBar addGestureRecognizer:self.longPressRecognizer];
    
    return self;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    _viewWillAppearOnce = YES;
    [self loadChildren:self.pendingChildViewControllers];
}

- (void)onChildAddToParent:(UIViewController *)child options:(RNNNavigationOptions *)options {
    [_bottomTabPresenter applyOptionsOnWillMoveToParentViewController:options child:child];
}

- (void)mergeChildOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    [super mergeChildOptions:options child:child];
    UIViewController* childViewController = [self findViewController:child];
    [_bottomTabPresenter mergeOptions:options resolvedOptions:childViewController.resolveOptions child:childViewController];
    [_dotIndicatorPresenter mergeOptions:options resolvedOptions:childViewController.resolveOptions child:childViewController];
}

- (id<UITabBarControllerDelegate>)delegate {
	return self;
}

- (void)render {
    [_bottomTabsAttacher attach:self];
}

- (void)viewDidLayoutSubviews {
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
    NSArray* children = self.pendingChildViewControllers ?: self.childViewControllers;
	for (id child in children) {
		UIViewController<RNNLayoutProtocol>* vc = child;

		if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] && [vc.layoutInfo.componentId isEqualToString:componentID]) {
            NSUInteger selectedIndex = [children indexOfObject:child];
			[self setSelectedIndex:selectedIndex];
            _currentTabIndex = selectedIndex;
		}
	}
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
	_currentTabIndex = selectedIndex;
	[super setSelectedIndex:selectedIndex];
}

- (UIViewController *)selectedViewController {
    NSArray* children = self.pendingChildViewControllers ?: self.childViewControllers;
    return children.count ? children[_currentTabIndex] : nil;
}

- (void)setSelectedViewController:(__kindof UIViewController *)selectedViewController {
    NSArray* children = self.pendingChildViewControllers ?: self.childViewControllers;
    _previousTabIndex = _currentTabIndex;
    _currentTabIndex = [children indexOfObject:selectedViewController];
    [super setSelectedViewController:selectedViewController];
}

- (void)loadChildren:(NSArray *)children {
    if (self.viewWillAppearOnce) {
        [super loadChildren:children];
        self.pendingChildViewControllers = nil;
    }
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
    for (UITabBarItem* item in self.tabBar.items) {
        if (CGRectContainsPoint([[item valueForKey:@"view"] frame], locationInTabBar)) {
            NSUInteger tabIndex = [self.tabBar.items indexOfObject:item];
            [self.eventEmitter sendBottomTabLongPressed:@(tabIndex)];
            break;
        }
    }
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
	[self.eventEmitter sendBottomTabSelected:@(tabBarController.selectedIndex) unselected:@(_previousTabIndex)];
}

- (void)handleLongPressGesture:(UILongPressGestureRecognizer *)recognizer {
    if (recognizer.state == UIGestureRecognizerStateBegan) {
        CGPoint locationInTabBar = [recognizer locationInView:self.tabBar];
        [self handleTabBarLongPress:locationInTabBar];
    }
}

- (BOOL)tabBarController:(UITabBarController *)tabBarController shouldSelectViewController:(UIViewController *)viewController
{
    NSUInteger _index = [tabBarController.viewControllers indexOfObject:viewController];
    [self.eventEmitter sendBottomTabPressed:@(_index)];
    
    if([[viewController resolveOptions].bottomTab.selectTabOnPress getWithDefaultValue:YES]){
        return YES;
    }

    return NO;
}

# pragma mark - UIViewController overrides

- (void)willMoveToParentViewController:(UIViewController *)parent {
    [self.presenter willMoveToParentViewController:parent];
}

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
