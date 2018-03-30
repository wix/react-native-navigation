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
	
	if (self.visible) {
		[((RNNTabBarController *)viewController.tabBarController) setTabBarHidden:![self.visible boolValue] animated:[self.animate boolValue]];
	} else {
		[((RNNTabBarController *)viewController.tabBarController) setTabBarHidden:NO animated:NO];
	}
	
	if (self.testID) {
		viewController.tabBarController.tabBar.accessibilityIdentifier = self.testID;
	}
	
	if (self.drawBehind) {
		if ([self.drawBehind boolValue]) {
			viewController.edgesForExtendedLayout |= UIRectEdgeBottom;
		} else {
			viewController.edgesForExtendedLayout &= ~UIRectEdgeBottom;
		}
	}
	
	if (self.backgroundColor) {
		viewController.tabBarController.tabBar.barTintColor = [RCTConvert UIColor:self.backgroundColor];
	} else {
		viewController.tabBarController.tabBar.barTintColor = nil;
	}
	
	if (self.translucent) {
		viewController.tabBarController.tabBar.translucent = [self.translucent boolValue];
	} else {
		viewController.tabBarController.tabBar.translucent = NO;
	}
	
	if (self.hideShadow) {
		viewController.tabBarController.tabBar.clipsToBounds = [self.hideShadow boolValue];
	}
	
	if (self.tabBarTextFont) {
		NSMutableDictionary* tabBarTitleTextAttributes = [NSMutableDictionary new];
		tabBarTitleTextAttributes[NSFontAttributeName] = self.tabBarTextFont;
		
		for (UITabBarItem* item in viewController.tabBarController.tabBar.items) {
			[item setTitleTextAttributes:tabBarTitleTextAttributes forState:UIControlStateNormal];
		}
	}
	
	if (self.tabColor) {
		viewController.tabBarController.tabBar.unselectedItemTintColor = [RCTConvert UIColor:self.tabColor];
	}
	
	if (self.selectedTabColor) {
		viewController.tabBarController.tabBar.tintColor = [RCTConvert UIColor:self.selectedTabColor];
	}
	
	[self resetOptions];
}

-(UIFont *)tabBarTextFont {
	if (self.fontFamily) {
		return [UIFont fontWithName:self.fontFamily size:self.tabBarTextFontSizeValue];
	}
	else if (self.fontSize) {
		return [UIFont systemFontOfSize:self.tabBarTextFontSizeValue];
	}
	else {
		return nil;
	}
}

-(CGFloat)tabBarTextFontSizeValue {
	return self.fontSize ? [self.fontSize floatValue] : 10;
}

- (void)resetOptions {
	self.currentTabId = nil;
	self.currentTabIndex = nil;
}

@end
