
#import "RNNTabBarController.h"

#define kTabBarHiddenDuration 0.3

@implementation RNNTabBarController {
	NSUInteger _currentTabIndex;
	RNNEventEmitter *_eventEmitter;
}

- (instancetype)initWithEventEmitter:(id)eventEmitter {
	self = [super init];
	_eventEmitter = eventEmitter;
	self.delegate = self;
	return self;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.selectedViewController.supportedInterfaceOrientations;
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
	for (id child in self.childViewControllers) {
		RNNRootViewController* vc = child;

		if ([vc.componentId isEqualToString:componentID]) {
			[self setSelectedIndex:[self.childViewControllers indexOfObject:child]];
		}
	}
}

- (void)setSelectedIndex:(NSUInteger)selectedIndex {
	_currentTabIndex = selectedIndex;
	[super setSelectedIndex:selectedIndex];
}

- (void)mergeOptions:(RNNOptions *)options {
	[self.getLeafViewController mergeOptions:options];
}

- (UIViewController *)getLeafViewController {
	return ((UIViewController<RNNRootViewProtocol>*)self.selectedViewController).getLeafViewController;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return ((UIViewController<RNNRootViewProtocol>*)self.selectedViewController).preferredStatusBarStyle;
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
	[_eventEmitter sendBottomTabSelected:@(tabBarController.selectedIndex) unselected:@(_currentTabIndex)];
	_currentTabIndex = tabBarController.selectedIndex;
}

@end
