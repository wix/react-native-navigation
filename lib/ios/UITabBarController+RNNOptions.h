#import <UIKit/UIKit.h>

@interface UITabBarController (RNNOptions)

- (void)rnn_setCurrentTabIndex:(NSUInteger)currentTabIndex;

- (void)rnn_setCurrentTabID:(NSString *)tabID;

- (void)rnn_setTabBarTestID:(NSString *)testID;

- (void)rnn_setTabBarBackgroundColor:(UIColor *)backgroundColor;

#if !TARGET_OS_TV
- (void)rnn_setTabBarStyle:(UIBarStyle)barStyle;
#endif

- (void)rnn_setTabBarTranslucent:(BOOL)translucent;

- (void)rnn_setTabBarHideShadow:(BOOL)hideShadow;

- (void)rnn_setTabBarVisible:(BOOL)visible;

@end
