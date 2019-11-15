#import <UIKit/UIKit.h>

@interface UITabBarController (RNNOptions)

- (void)setCurrentTabIndex:(NSUInteger)currentTabIndex;

- (void)setCurrentTabID:(NSString *)tabID;

- (void)setTabBarTestID:(NSString *)testID;

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor;

- (void)setTabBarStyle:(UIBarStyle)barStyle;

- (void)setTabBarTranslucent:(BOOL)translucent;

- (void)setTabBarHideShadow:(BOOL)hideShadow;

- (void)setTabBarVisible:(BOOL)visible animated:(BOOL)animated;

@end
