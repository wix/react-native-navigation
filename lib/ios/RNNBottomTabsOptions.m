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
	
	if (self.backgroundColor) {
		viewController.tabBarController.tabBar.barTintColor = [RCTConvert UIColor:self.backgroundColor];
	}
	
	if (self.translucent) {
		viewController.tabBarController.tabBar.translucent = [self.translucent boolValue];
	}
	
	if (self.hideShadow) {
		viewController.tabBarController.tabBar.clipsToBounds = [self.hideShadow boolValue];
	}
	
	if (self.tabBarTextFont || self.textColor) {
		NSMutableDictionary* tabBarTitleTextAttributes = [NSMutableDictionary new];
		if (self.textColor) {
			tabBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.textColor];
		}
		
		if (self.tabBarTextFont) {
			tabBarTitleTextAttributes[NSFontAttributeName] = self.tabBarTextFont;
		}
		
		for (UITabBarItem* item in viewController.tabBarController.tabBar.items) {
			[item setTitleTextAttributes:tabBarTitleTextAttributes forState:UIControlStateNormal];
		}
	}
	
	if (self.selectedTextColor){
		for (UITabBarItem* item in viewController.tabBarController.tabBar.items) {
			NSMutableDictionary* tabBarTitleTextAttributes = [NSMutableDictionary new];
			tabBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.selectedTextColor];
			[item setTitleTextAttributes:tabBarTitleTextAttributes forState:UIControlStateSelected];
		}
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
