#import "RNNBottomTabsController.h"
#import "UITabBarController+RNNUtils.h"

@implementation RNNBottomTabsController {
	NSUInteger _currentTabIndex;
    BottomTabsBaseAttacher* _bottomTabsAttacher;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter
              childViewControllers:(NSArray *)childViewControllers
                bottomTabsAttacher:(BottomTabsBaseAttacher *)bottomTabsAttacher {
    self = [super initWithLayoutInfo:layoutInfo creator:creator options:options defaultOptions:defaultOptions presenter:presenter eventEmitter:eventEmitter childViewControllers:childViewControllers];
    _bottomTabsAttacher = bottomTabsAttacher;
    return self;
}

- (id<UITabBarControllerDelegate>)delegate {
	return self;
}

- (void)render {
    [_bottomTabsAttacher attach:self];
}

- (void)viewDidLayoutSubviews {
    [self.presenter viewDidLayoutSubviews];
    
    for (UIView *view in [[self tabBar] subviews]) {
         UILongPressGestureRecognizer *longPressGesture = [[UILongPressGestureRecognizer alloc] initWithTarget: self action: @selector(handleLongPress:)];
          if ([NSStringFromClass([view class]) isEqualToString:@"UITabBarButton"]) {
              [view addGestureRecognizer: longPressGesture];
          }
    }
}

- (UIViewController *)getCurrentChild {
	return self.selectedViewController;
}

- (CGFloat)getBottomTabsHeight {
    return self.tabBar.frame.size.height;
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
	for (id child in self.childViewControllers) {
		UIViewController<RNNLayoutProtocol>* vc = child;

		if ([vc conformsToProtocol:@protocol(RNNLayoutProtocol)] && [vc.layoutInfo.componentId isEqualToString:componentID]) {
			[self setSelectedIndex:[self.childViewControllers indexOfObject:child]];
		}
	}
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
	_currentTabIndex = selectedIndex;
	[super setSelectedIndex:selectedIndex];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return [[self presenter] getStatusBarStyle:self.resolveOptions];
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
	[self.eventEmitter sendBottomTabSelected:@(tabBarController.selectedIndex) unselected:@(_currentTabIndex)];
	_currentTabIndex = tabBarController.selectedIndex;
}

- (void)handleLongPress:(UILongPressGestureRecognizer *) recognizer {
    if (recognizer.state == UIGestureRecognizerStateBegan) {
        NSUInteger _index = [self.tabBar.subviews indexOfObject:(UIView *)recognizer.view];
        [self.eventEmitter sendBottomTabLongPressed:@(_index)];
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

@end
