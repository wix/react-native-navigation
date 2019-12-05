#import "RNNBottomTabsController.h"
#import "UITabBarController+RNNUtils.h"
#import "BottomTabsAttacher.h"

@implementation RNNBottomTabsController {
	NSUInteger _currentTabIndex;
    BottomTabsAttacher* _bottomTabsAttacher;
}

- (instancetype)init {
    self = [super init];
    _bottomTabsAttacher = [[BottomTabsAttacher alloc] init];
    return self;
}

- (id<UITabBarControllerDelegate>)delegate {
	return self;
}

- (void)render {
    BottomTabsAttachMode attachMode = [RCTConvert BottomTabsAttachMode:[self.resolveOptionsWithDefault.bottomTabs.tabsAttachMode getWithDefaultValue:@"together"]];
    [_bottomTabsAttacher attach:self withMode:attachMode];
}

- (void)viewDidLayoutSubviews {
	[self.presenter viewDidLayoutSubviews];
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

@end
