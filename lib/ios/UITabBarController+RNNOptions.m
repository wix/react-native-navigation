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

- (void)rnn_setTabBarStyle:(UIBarStyle)barStyle {
	self.tabBar.barStyle = barStyle;
}

- (void)rnn_setTabBarTranslucent:(BOOL)translucent {
	self.tabBar.translucent = translucent;
}

- (void)rnn_setTabBarHideShadow:(BOOL)hideShadow {
	self.tabBar.clipsToBounds = hideShadow;
}

- (void)rnn_setTabBarVisible:(BOOL)visible animated:(BOOL)animated {
    const CGRect tabBarFrame = self.tabBar.frame;
	const CGRect tabBarVisibleFrame = CGRectMake(tabBarFrame.origin.x,
												 self.view.frame.size.height - tabBarFrame.size.height,
												 tabBarFrame.size.width,
												 tabBarFrame.size.height);
	const CGRect tabBarHiddenFrame = CGRectMake(tabBarFrame.origin.x,
												self.view.frame.size.height,
												tabBarFrame.size.width,
												tabBarFrame.size.height);
	if (!animated) {
		self.tabBar.hidden = !visible;
		self.tabBar.frame = visible ? tabBarVisibleFrame : tabBarHiddenFrame;
		return;
	}
	static const CGFloat animationDuration = 0.15;

	if (visible) {
		self.tabBar.hidden = NO;
		[UIView animateWithDuration: animationDuration
							  delay: 0
							options: UIViewAnimationOptionCurveEaseOut
						 animations:^()
		 {
			 self.tabBar.frame = tabBarVisibleFrame;
		 }
						 completion:^(BOOL finished)
		 {}];
	} else {
		[UIView animateWithDuration: animationDuration
							  delay: 0
							options: UIViewAnimationOptionCurveEaseIn
						 animations:^()
		 {
			 self.tabBar.frame = tabBarHiddenFrame;
		 }
						 completion:^(BOOL finished)
		 {
			 self.tabBar.hidden = YES;
		 }];
	}
}

@end
