#import "RNNTabBarPresenter.h"
#import "UITabBarController+RNNOptions.h"

@implementation RNNTabBarPresenter

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
	UITabBarController* tabBarController = self.bindedViewController;
	[tabBarController rnn_setCurrentTabIndex:[options.bottomTabs.currentTabIndex getWithDefaultValue:0]];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	UITabBarController* tabBarController = self.bindedViewController;
	
	[tabBarController rnn_setTabBarTestID:[options.bottomTabs.testID getWithDefaultValue:nil]];
	[tabBarController rnn_setTabBarBackgroundColor:[options.bottomTabs.backgroundColor getWithDefaultValue:nil]];
	[tabBarController rnn_setTabBarTranslucent:[options.bottomTabs.translucent getWithDefaultValue:NO]];
	[tabBarController rnn_setTabBarHideShadow:[options.bottomTabs.hideShadow getWithDefaultValue:NO]];
	[tabBarController rnn_setTabBarStyle:[RCTConvert UIBarStyle:[options.bottomTabs.barStyle getWithDefaultValue:@"default"]]];
	[tabBarController rnn_setTabBarVisible:[options.bottomTabs.visible getWithDefaultValue:YES] animated:[options.bottomTabs.animate getWithDefaultValue:NO]];
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super mergeOptions:newOptions currentOptions:currentOptions defaultOptions:defaultOptions];
	
	UITabBarController* tabBarController = self.bindedViewController;
	
	if (newOptions.bottomTabs.currentTabIndex.hasValue) {
		[tabBarController rnn_setCurrentTabIndex:newOptions.bottomTabs.currentTabIndex.get];
		[newOptions.bottomTabs.currentTabIndex consume];
	}
	
	if (newOptions.bottomTabs.currentTabId.hasValue) {
		[tabBarController rnn_setCurrentTabID:newOptions.bottomTabs.currentTabId.get];
		[newOptions.bottomTabs.currentTabId consume];
	}
	
	if (newOptions.bottomTabs.testID.hasValue) {
		[tabBarController rnn_setTabBarTestID:newOptions.bottomTabs.testID.get];
	}
	
	if (newOptions.bottomTabs.backgroundColor.hasValue) {
		[tabBarController rnn_setTabBarBackgroundColor:newOptions.bottomTabs.backgroundColor.get];
	}
	
	if (newOptions.bottomTabs.barStyle.hasValue) {
		[tabBarController rnn_setTabBarStyle:[RCTConvert UIBarStyle:newOptions.bottomTabs.barStyle.get]];
	}
	
	if (newOptions.bottomTabs.translucent.hasValue) {
		[tabBarController rnn_setTabBarTranslucent:newOptions.bottomTabs.translucent.get];
	}
	
	if (newOptions.bottomTabs.hideShadow.hasValue) {
		[tabBarController rnn_setTabBarHideShadow:newOptions.bottomTabs.hideShadow.get];
	}
	
	if (newOptions.bottomTabs.visible.hasValue) {
		if (newOptions.bottomTabs.animate.hasValue) {
			[tabBarController rnn_setTabBarVisible:newOptions.bottomTabs.visible.get animated:[newOptions.bottomTabs.animate getWithDefaultValue:NO]];
		} else {
			[tabBarController rnn_setTabBarVisible:newOptions.bottomTabs.visible.get animated:NO];
		}
	}
}

@end
