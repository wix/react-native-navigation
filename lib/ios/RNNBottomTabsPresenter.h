#import "RNNBasePresenter.h"

@interface RNNBottomTabsPresenter : RNNBasePresenter

- (void)applyDotIndicator;

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor;

- (UITabBarController *)tabBarController;

- (UITabBar *)tabBar;

@end
