
#import "RNNTabBarController.h"
#import "RNNRootViewController.h"
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

- (void)setTabBarHidden:(BOOL)hidden animated:(BOOL)animated {
	CGRect frame = self.tabBar.frame;
	CGFloat height = frame.size.height;
	CGFloat offsetY = (hidden ? self.view.frame.size.height : self.view.frame.size.height-height);
	frame.origin.y = offsetY;
	NSTimeInterval duration = animated ? kTabBarHiddenDuration : 0.0;
	
	[UIView animateWithDuration:duration animations:^{
		self.tabBar.frame = frame;
	}];
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
	for (id child in self.childViewControllers) {
		RNNRootViewController* vc = child;

		if ([vc.componentId isEqualToString:componentID]) {
			[self setSelectedIndex:[self.childViewControllers indexOfObject:child]];
		}
	}
}

- (BOOL)isCustomTransitioned {
	return NO;
}

- (RNNOptions *)options {
	return nil;
}

- (void)mergeOptions:(NSDictionary *)options {
	[((UIViewController<RNNRootViewProtocol>*)self.selectedViewController) mergeOptions:options];
}

- (NSString *)componentId {
	return ((UIViewController<RNNRootViewProtocol>*)self.selectedViewController).componentId;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return ((UIViewController<RNNRootViewProtocol>*)self.selectedViewController).preferredStatusBarStyle;
}

#pragma mark UITabBarControllerDelegate

- (void)tabBarController:(UITabBarController *)tabBarController didSelectViewController:(UIViewController *)viewController {
	[_eventEmitter sendOnNavigationEvent:@"bottomTabSelected" params:@{@"selectedTabIndex": @(tabBarController.selectedIndex), @"unselectedTabIndex": @(_currentTabIndex)}];
	
	_currentTabIndex = tabBarController.selectedIndex;
}

@end
