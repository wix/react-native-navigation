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

- (BOOL)rnn_isTabBarHidden;

/**
 * YES when UIKit renders the tab bar in the vertical bar (iPhone Duo, iOS 27.1+). `tabBar` is then
 * an empty placeholder along the leading or trailing edge, and the tabs are drawn by the system.
 */
- (BOOL)rnn_isTabBarVertical;

- (void)syncTabBarItemTestIDs;

@end
