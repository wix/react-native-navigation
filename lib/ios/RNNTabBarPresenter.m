#import "RNNTabBarPresenter.h"
#import "UITabBarController+RNNOptions.h"

@implementation RNNTabBarPresenter

- (void)applyOptions:(RNNNavigationOptions *)initialOptions {
	[super applyOptions:initialOptions];
	
	RNNNavigationOptions* options = [initialOptions withDefault:self.defaultOptions];
	
	UITabBarController* tabBarController = self.bindedViewController;
	
	[tabBarController rnn_setTabBarTestID:[options.bottomTabs.testID getWithDefaultValue:nil]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
	[super mergeOptions:options resolvedOptions:resolvedOptions];
	
	UITabBarController* tabBarController = self.bindedViewController;
	
	if (options.bottomTabs.currentTabIndex.hasValue) {
		[tabBarController rnn_setCurrentTabIndex:options.bottomTabs.currentTabIndex.get];
		[options.bottomTabs.currentTabIndex consume];
	}
	
	if (options.bottomTabs.currentTabId.hasValue) {
		[tabBarController rnn_setCurrentTabID:options.bottomTabs.currentTabId.get];
		[options.bottomTabs.currentTabId consume];
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
