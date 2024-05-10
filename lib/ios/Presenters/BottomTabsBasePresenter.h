#import "RNNBasePresenter.h"
#import "UIColor+RNNUtils.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+Utils.h"
#import <Foundation/Foundation.h>

@interface BottomTabsBasePresenter : RNNBasePresenter

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent;

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor;

- (void)setTabBarTranslucent:(BOOL)translucent;

- (void)applyTabBarBorder:(RNNBottomTabsOptions *)options;

- (UITabBarController *)tabBarController;

- (UITabBar *)tabBar;

@end
