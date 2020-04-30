#import <UIKit/UIKit.h>

@interface UITabBarController (RNNOptions)

- (void)setCurrentTabIndex:(NSUInteger)currentTabIndex;

- (void)setCurrentTabID:(NSString *)tabID;

- (void)setTabBarTestID:(NSString *)testID;
#if !TARGET_OS_TV
- (void)setTabBarStyle:(UIBarStyle)barStyle;
#endif

- (void)setTabBarTranslucent:(BOOL)translucent;

- (void)setTabBarHideShadow:(BOOL)hideShadow;

- (void)setTabBarVisible:(BOOL)visible animated:(BOOL)animated;

- (void)centerTabItems;

@end
