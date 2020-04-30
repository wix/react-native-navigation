#import <UIKit/UIKit.h>

@class RNNBottomTabOptions;
@class RNNNavigationOptions;
@class RNNBackButtonOptions;

@interface UIViewController (RNNOptions)

- (void)setBackgroundImage:(UIImage *)backgroundImage;

- (void)setSearchBarWithPlaceholder:(NSString *)placeholder hideNavBarOnFocusSearchBar:(BOOL)hideNavBarOnFocusSearchBar;

- (void)setSearchBarHiddenWhenScrolling:(BOOL)searchBarHidden;

- (void)setDrawBehindTopBar:(BOOL)drawBehind;

- (void)setDrawBehindTabBar:(BOOL)drawBehindTabBar;

- (void)setTabBarItemBadgeColor:(UIColor *)badgeColor;

- (void)setTabBarItemBadge:(NSString *)badge;
#if !TARGET_OS_TV
- (void)setTopBarPrefersLargeTitle:(BOOL)prefersLargeTitle;
- (void)setBackButtonVisible:(BOOL)visible;
- (void)setStatusBarBlur:(BOOL)blur;

#endif
- (void)setNavigationItemTitle:(NSString *)title;

- (void)setStatusBarStyle:(NSString *)style animated:(BOOL)animated;



- (void)setBackgroundColor:(UIColor *)backgroundColor;

- (void)setInterceptTouchOutside:(BOOL)interceptTouchOutside;

- (BOOL)isModal;

@end
