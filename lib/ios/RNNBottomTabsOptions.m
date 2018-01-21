#import "RNNBottomTabsOptions.h"
#import "RNNTabBarController.h"
extern const NSInteger BLUR_TOPBAR_TAG;

@implementation RNNBottomTabsOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.currentTabIndex) {
		[viewController.tabBarController setSelectedIndex:[self.currentTabIndex unsignedIntegerValue]];
	}
	
	if (self.currentTabId) {
		[(RNNTabBarController*)viewController.tabBarController setSelectedIndexByComponentID:self.currentTabId];
	}
	
	if (self.hidden) {
		[((RNNTabBarController *)viewController.tabBarController) setTabBarHidden:[self.hidden boolValue] animated:[self.animateHide boolValue]];
	}
	
	if (self.testID) {
		viewController.tabBarController.tabBar.accessibilityIdentifier = self.testID;
	}
	
	if (self.drawUnder) {
		if ([self.drawUnder boolValue]) {
			viewController.edgesForExtendedLayout |= UIRectEdgeBottom;
		} else {
			viewController.edgesForExtendedLayout &= ~UIRectEdgeBottom;
		}
	}
	
	if (self.selectedTabColor) {
		viewController.tabBarController.tabBar.tintColor = [UIColor redColor];
	}
	
	if (self.unselectedTabColor) {
		viewController.tabBarController.tabBar.unselectedItemTintColor = [UIColor greenColor];
	}
	
	if (self.backgroundColor) {
		viewController.tabBarController.tabBar.barTintColor = [UIColor grayColor];
	}
	
	
	[self resetOptions];
}

- (void)resetOptions {
	self.currentTabId = nil;
	self.currentTabIndex = nil;
}

@end
