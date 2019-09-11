#import "UITabBarController+RNNOptions.h"
#import "RNNBottomTabsController.h"

@implementation UITabBarController (RNNOptions)

- (void)setCurrentTabIndex:(NSUInteger)currentTabIndex {
	[self setSelectedIndex:currentTabIndex];
}

- (void)setCurrentTabID:(NSString *)currentTabId {
	[(RNNBottomTabsController*)self setSelectedIndexByComponentID:currentTabId];
}

- (void)setTabBarTestID:(NSString *)testID {
	self.tabBar.accessibilityIdentifier = testID;
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
	self.tabBar.barTintColor = backgroundColor;
}

- (void)setTabBarStyle:(UIBarStyle)barStyle {
	self.tabBar.barStyle = barStyle;
}

- (void)setTabBarTranslucent:(BOOL)translucent {
	self.tabBar.translucent = translucent;
}

- (void)setTabBarHideShadow:(BOOL)hideShadow {
	self.tabBar.clipsToBounds = hideShadow;
}

- (void)setTabBarVisible:(BOOL)visible animated:(BOOL)animated {
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

- (void)forEachTab:(void (^)(UIView *, UIViewController * tabViewController, int tabIndex))performOnTab {
    int tabIndex = 0;
    for (UIView * tab in self.tabBar.subviews) {
        if ([NSStringFromClass([tab class]) isEqualToString:@"UITabBarButton"]) {
            performOnTab(tab, [self childViewControllers][(NSUInteger) tabIndex], tabIndex);
            tabIndex++;
        }
    }
}

@end
