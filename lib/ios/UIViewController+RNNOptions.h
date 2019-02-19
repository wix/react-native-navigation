#import <UIKit/UIKit.h>

@interface UIViewController (RNNOptions)

- (void)rnn_setBackgroundImage:(UIImage *)backgroundImage;

- (void)rnn_setModalPresentationStyle:(UIModalPresentationStyle)modalPresentationStyle;

- (void)rnn_setModalTransitionStyle:(UIModalTransitionStyle)modalTransitionStyle;
#if !TARGET_OS_TV
- (void)rnn_setSearchBarWithPlaceholder:(NSString *)placeholder hideNavBarOnFocusSearchBar:(BOOL)hideNavBarOnFocusSearchBar;

- (void)rnn_setSearchBarHiddenWhenScrolling:(BOOL)searchBarHidden;
#endif
- (void)rnn_setDrawBehindTopBar:(BOOL)drawBehind;

- (void)rnn_setDrawBehindTabBar:(BOOL)drawBehindTabBar;

- (void)rnn_setTabBarItemBadgeColor:(UIColor *)badgeColor;

- (void)rnn_setTabBarItemBadge:(NSString *)badge;
#if !TARGET_OS_TV
- (void)rnn_setTopBarPrefersLargeTitle:(BOOL)prefersLargeTitle;
#endif
- (void)rnn_setNavigationItemTitle:(NSString *)title;

#if !TARGET_OS_TV
- (void)rnn_setStatusBarStyle:(NSString *)style animated:(BOOL)animated;

- (void)rnn_setStatusBarBlur:(BOOL)blur;

- (void)rnn_setBackButtonVisible:(BOOL)visible;
#endif
- (void)rnn_setBackgroundColor:(UIColor *)backgroundColor;

- (void)rnn_setInterceptTouchOutside:(BOOL)interceptTouchOutside;

- (BOOL)isModal;

@end
