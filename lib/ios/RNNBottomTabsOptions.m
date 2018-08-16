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
	
	if (self.testID) {
		viewController.tabBarController.tabBar.accessibilityIdentifier = self.testID;
	}
	
	if (self.drawBehind) {
		if ([self.drawBehind boolValue]) {
			[viewController setExtendedLayoutIncludesOpaqueBars:YES];
			viewController.edgesForExtendedLayout |= UIRectEdgeBottom;
		} else {
			[viewController setExtendedLayoutIncludesOpaqueBars:NO];
			viewController.edgesForExtendedLayout &= ~UIRectEdgeBottom;
		}
	}
	
	if (self.backgroundColor) {
		viewController.tabBarController.tabBar.barTintColor = [RCTConvert UIColor:self.backgroundColor];
	} else {
		viewController.tabBarController.tabBar.barTintColor = nil;
	}
	
	if (self.barStyle) {
		viewController.tabBarController.tabBar.barStyle = [RCTConvert UIBarStyle:self.barStyle];
	} else {
		viewController.tabBarController.tabBar.barStyle = UIBarStyleDefault;
	}

	if (self.translucent) {
		viewController.tabBarController.tabBar.translucent = [self.translucent boolValue];
	} else {
		viewController.tabBarController.tabBar.translucent = NO;
	}
	
	if (self.hideShadow) {
		viewController.tabBarController.tabBar.clipsToBounds = [self.hideShadow boolValue];
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
