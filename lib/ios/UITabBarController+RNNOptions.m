#import "UITabBarController+RNNOptions.h"
#import "RNNTabBarController.h"

@implementation UITabBarController (RNNOptions)

- (void)rnn_setCurrentTabIndex:(NSUInteger)currentTabIndex {
	[self setSelectedIndex:currentTabIndex];
}

- (void)rnn_setCurrentTabID:(NSString *)currentTabId {
	[(RNNTabBarController*)self setSelectedIndexByComponentID:currentTabId];
}

- (void)rnn_setTabBarTestID:(NSString *)testID {
	self.tabBar.accessibilityIdentifier = testID;
}

- (void)rnn_setTabBarBackgroundColor:(UIColor *)backgroundColor {
	self.tabBar.barTintColor = backgroundColor;
}

#if !TARGET_OS_TV
- (void)rnn_setTabBarStyle:(UIBarStyle)barStyle {
	self.tabBar.barStyle = barStyle;
}
#endif

- (void)rnn_setTabBarTranslucent:(BOOL)translucent {
	self.tabBar.translucent = translucent;
}

- (void)rnn_setTabBarHideShadow:(BOOL)hideShadow {
	self.tabBar.clipsToBounds = hideShadow;
}

- (void)rnn_setTabBarVisible:(BOOL)visible {
	self.tabBar.hidden = !visible;
}

@end
