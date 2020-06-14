#import "UITabBarController+RNNOptions.h"
#import "RNNBottomTabsController.h"
#import "UITabBar+utils.h"

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

- (void)setTabBarStyle:(UIBarStyle)barStyle {
	self.tabBar.barStyle = barStyle;
}

- (void)setTabBarTranslucent:(BOOL)translucent {
	self.tabBar.translucent = translucent;
}

- (void)setTabBarHideShadow:(BOOL)hideShadow {
	self.tabBar.clipsToBounds = hideShadow;
}

- (void)centerTabItems {
	[self.tabBar centerTabItems];
}


- (void)showTabBar:(BOOL)animated {
    static const CGFloat animationDuration = 0.15;
    const CGRect tabBarVisibleFrame = CGRectMake(self.tabBar.frame.origin.x,
                                                 self.view.frame.size.height - self.tabBar.frame.size.height,
                                                 self.tabBar.frame.size.width,
                                                 self.tabBar.frame.size.height);
    self.tabBar.hidden = NO;
    if (!animated) {
        self.tabBar.frame = tabBarVisibleFrame;
    } else {
        [UIView animateWithDuration: animationDuration
                              delay: 0
                            options: UIViewAnimationOptionCurveEaseOut
                         animations:^()
         {
            self.tabBar.frame = tabBarVisibleFrame;
        } completion:^(BOOL finished)
         {}];
    }
}

- (void)hideTabBar:(BOOL)animated {
    static const CGFloat animationDuration = 0.15;
    const CGRect tabBarHiddenFrame = CGRectMake(self.tabBar.frame.origin.x,
                                                self.view.frame.size.height,
                                                self.tabBar.frame.size.width,
                                                self.tabBar.frame.size.height);
    
    if (!animated) {
        self.tabBar.frame = tabBarHiddenFrame;
        self.tabBar.hidden = YES;
    } else {
        [UIView animateWithDuration: animationDuration
                              delay: 0
                            options: UIViewAnimationOptionCurveEaseOut
                         animations:^()
         {
            self.tabBar.frame = tabBarHiddenFrame;
        } completion:^(BOOL finished)
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
