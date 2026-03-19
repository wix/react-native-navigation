#import "RNNBottomTabsController.h"
#import "UITabBar+utils.h"
#import "UITabBarController+RNNOptions.h"
#import <objc/runtime.h>

static const void *RNNTabBarTestIDRetryScheduledKey = &RNNTabBarTestIDRetryScheduledKey;

@implementation UITabBarController (RNNOptions)

- (void)rnn_applyTestID:(NSString *)testID toTabView:(UIView *)tabView {
    tabView.accessibilityIdentifier = testID;
}

- (BOOL)rnn_isTabBarTestIDRetryScheduled {
    return [objc_getAssociatedObject(self, RNNTabBarTestIDRetryScheduledKey) boolValue];
}

- (void)rnn_setTabBarTestIDRetryScheduled:(BOOL)scheduled {
    objc_setAssociatedObject(self, RNNTabBarTestIDRetryScheduledKey, @(scheduled),
                             OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (BOOL)rnn_applyTabBarItemTestIDs {
    NSArray<UITabBarItem *> *items = self.tabBar.items ?: @[];
    BOOL appliedAllKnownTestIDs = YES;

    for (NSUInteger tabIndex = 0; tabIndex < items.count; tabIndex++) {
        UITabBarItem *item = items[tabIndex];
        NSString *testID = item.accessibilityIdentifier;
        UIView *tabView = [self.tabBar tabBarItemViewAtIndex:tabIndex];
        if (testID.length > 0 && tabView) {
            [self rnn_applyTestID:testID toTabView:tabView];
        } else if (testID.length > 0) {
            appliedAllKnownTestIDs = NO;
        }
    }

    return appliedAllKnownTestIDs;
}

- (void)setCurrentTabIndex:(NSUInteger)currentTabIndex {
    [self setSelectedIndex:currentTabIndex];
}

- (void)setCurrentTabID:(NSString *)currentTabId {
    [(RNNBottomTabsController *)self setSelectedIndexByComponentID:currentTabId];
}

- (void)setTabBarTestID:(NSString *)testID {
    self.tabBar.accessibilityIdentifier = testID;
}

- (void)syncTabBarItemTestIDs {
    if ([self rnn_applyTabBarItemTestIDs]) {
        [self rnn_setTabBarTestIDRetryScheduled:NO];
        return;
    }

    if ([self rnn_isTabBarTestIDRetryScheduled])
        return;

    [self rnn_setTabBarTestIDRetryScheduled:YES];

    __weak UITabBarController *weakSelf = self;
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.15 * NSEC_PER_SEC)),
                   dispatch_get_main_queue(), ^{
        UITabBarController *controller = weakSelf;
        if (!controller)
            return;

        [controller rnn_setTabBarTestIDRetryScheduled:NO];
        [controller rnn_applyTabBarItemTestIDs];
    });
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
    const CGRect tabBarVisibleFrame = CGRectMake(
        self.tabBar.frame.origin.x, self.view.frame.size.height - self.tabBar.frame.size.height,
        self.tabBar.frame.size.width, self.tabBar.frame.size.height);
    self.tabBar.hidden = NO;
    if (!animated) {
        self.tabBar.frame = tabBarVisibleFrame;
    } else {
        [UIView animateWithDuration:animationDuration
                              delay:0
                            options:UIViewAnimationOptionCurveEaseOut
                         animations:^() {
                           self.tabBar.frame = tabBarVisibleFrame;
                         }
                         completion:^(BOOL finished){
                         }];
    }
}

- (void)hideTabBar:(BOOL)animated {
    static const CGFloat animationDuration = 0.15;
    const CGRect tabBarHiddenFrame =
        CGRectMake(self.tabBar.frame.origin.x, self.view.frame.size.height,
                   self.tabBar.frame.size.width, self.tabBar.frame.size.height);

    if (!animated) {
        self.tabBar.frame = tabBarHiddenFrame;
        self.tabBar.hidden = YES;
    } else {
        [UIView animateWithDuration:animationDuration
            delay:0
            options:UIViewAnimationOptionCurveEaseOut
            animations:^() {
              self.tabBar.frame = tabBarHiddenFrame;
            }
            completion:^(BOOL finished) {
              self.tabBar.hidden = YES;
            }];
    }
}

- (void)forEachTab:(void (^)(UIView *, UIViewController *tabViewController,
                             int tabIndex))performOnTab {
    int tabIndex = 0;
    for (UIView *tab in self.tabBar.subviews) {
        if ([NSStringFromClass([tab class]) isEqualToString:@"UITabBarButton"]) {
            performOnTab(tab, [self childViewControllers][(NSUInteger)tabIndex], tabIndex);
            tabIndex++;
        }
    }
}

@end
