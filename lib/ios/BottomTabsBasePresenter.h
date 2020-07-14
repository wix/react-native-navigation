#import <Foundation/Foundation.h>
#import "RNNBasePresenter.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+Utils.h"
#import "UIColor+RNNUtils.h"

@interface BottomTabsBasePresenter : RNNBasePresenter

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent;

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor;

- (void)setTabBarTranslucent:(BOOL)translucent;

- (UITabBarController *)tabBarController;

- (UITabBar *)tabBar;

@end
