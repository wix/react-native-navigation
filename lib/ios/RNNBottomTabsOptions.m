#import "RNNBottomTabsOptions.h"
#import "RNNTabBarController.h"
extern const NSInteger BLUR_TOPBAR_TAG;

@implementation RNNBottomTabsOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.currentTabIndex) {
		[viewController.tabBarController setSelectedIndex:[self.currentTabIndex unsignedIntegerValue]];
	}
	
	if (self.currentTabId) {
		[(RNNTabBarController*)viewController.tabBarController setSelectedIndexByContainerID:self.currentTabId];
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
	
	[self resetOptions];
}

- (void)resetOptions {
	self.currentTabId = nil;
	self.currentTabIndex = nil;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}
@end
