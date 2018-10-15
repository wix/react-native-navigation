#import "RNNTabBarPresenter.h"
#import "UITabBarController+RNNOptions.h"

@implementation RNNTabBarPresenter

- (void)applyOptions:(RNNNavigationOptions *)options {
	[super applyOptions:options];
	
	UITabBarController* tabBarController = self.bindedViewController;
	
	[tabBarController rnn_setTabBarTestID:[options.bottomTabs.testID getWithDefaultValue:nil]];
	[tabBarController rnn_setCurrentTabIndex:[options.bottomTabs.currentTabIndex getWithDefaultValue:0]];
	
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	[super mergeOptions:options];
	
	UITabBarController* tabBarController = self.bindedViewController;
	
	if (options.bottomTabs.currentTabIndex.hasValue) {
		[tabBarController rnn_setCurrentTabIndex:options.bottomTabs.currentTabIndex.get];
	}
	
	if (options.bottomTabs.currentTabId.hasValue) {
		[tabBarController rnn_setCurrentTabID:options.bottomTabs.currentTabId.get];
	}
	
	if (options.bottomTabs.testID.hasValue) {
		[tabBarController rnn_setTabBarTestID:options.bottomTabs.testID.get];
	}
	
	if (options.bottomTabs.backgroundColor.hasValue) {
		[tabBarController rnn_setTabBarBackgroundColor:options.bottomTabs.backgroundColor.get];
	}
	
	if (options.bottomTabs.barStyle.hasValue) {
		[tabBarController rnn_setTabBarStyle:[RCTConvert UIBarStyle:options.bottomTabs.barStyle.get]];
	}
	
	if (options.bottomTabs.translucent.hasValue) {
		[tabBarController rnn_setTabBarTranslucent:options.bottomTabs.translucent.get];
	}
	
	if (options.bottomTabs.hideShadow.hasValue) {
		[tabBarController rnn_setTabBarHideShadow:options.bottomTabs.hideShadow.get];
	}
}

@end
