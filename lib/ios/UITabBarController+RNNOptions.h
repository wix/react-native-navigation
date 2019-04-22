#import <UIKit/UIKit.h>

@interface UITabBarController (RNNOptions)

- (void)rnn_setCurrentTabIndex:(NSUInteger)currentTabIndex;

- (void)rnn_setCurrentTabID:(NSString *)tabID;

- (void)rnn_setTabBarTestID:(NSString *)testID;

- (void)rnn_setTabBarBackgroundColor:(UIColor *)backgroundColor;

- (void)rnn_setTabBarStyle:(UIBarStyle)barStyle;

- (void)rnn_setTabBarTranslucent:(BOOL)translucent;

- (void)rnn_setTabBarHideShadow:(BOOL)hideShadow;

- (void)rnn_setTabBarVisible:(BOOL)visible animated:(BOOL)animated;

@end
