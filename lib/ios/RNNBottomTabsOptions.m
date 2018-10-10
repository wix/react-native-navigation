#import "RNNBottomTabsOptions.h"
#import "RNNTabBarController.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+RNNOptions.h"

extern const NSInteger BLUR_TOPBAR_TAG;

@implementation RNNBottomTabsOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.currentTabIndex) {
		[viewController.tabBarController rnn_setCurrentTabIndex:[self.currentTabIndex unsignedIntegerValue]];
	}
	
	if (self.currentTabId) {
		[viewController.tabBarController rnn_setCurrentTabID:self.currentTabId];
	}
	
	if (self.testID) {
		[viewController.tabBarController rnn_setTabBarTestID:self.testID];
	}
	
	if (self.drawBehind) {
		[viewController rnn_setDrawBehindTabBar:[self.drawBehind boolValue]];
	}
	
	[self resetOptions];
}

- (void)applyOnTabBarController:(UITabBarController *)tabBarController {
	if (self.backgroundColor) {
		[tabBarController rnn_setTabBarBackgroundColor:[RCTConvert UIColor:self.backgroundColor]];
	}
	
	if (self.barStyle) {
		[tabBarController rnn_setTabBarStyle:[RCTConvert UIBarStyle:self.barStyle]];
	}
	
	if (self.translucent) {
		[tabBarController rnn_setTabBarTranslucent:[self.translucent boolValue]];
	}
	
	if (self.hideShadow) {
		[tabBarController rnn_setTabBarHideShadow:[self.hideShadow boolValue]];
	}

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
