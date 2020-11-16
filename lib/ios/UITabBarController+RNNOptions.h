#import <UIKit/UIKit.h>

@interface UITabBarController (RNNOptions)

- (void)setCurrentTabIndex:(NSUInteger)currentTabIndex;

- (void)setCurrentTabID:(NSString *)tabID;

- (void)setTabBarTestID:(NSString *)testID;

- (void)setTabBarStyle:(UIBarStyle)barStyle;

- (void)setTabBarTranslucent:(BOOL)translucent;

- (void)setTabBarHideShadow:(BOOL)hideShadow;

- (void)centerTabItems;

- (void)showTabBar:(BOOL)animated;

- (void)hideTabBar:(BOOL)animated;

@end
