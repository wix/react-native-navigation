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

- (void)setTopBarPrefersLargeTitle:(BOOL)prefersLargeTitle;

- (void)setNavigationItemTitle:(NSString *)title;

- (void)setStatusBarStyle:(NSString *)style animated:(BOOL)animated;

- (void)setStatusBarBlur:(BOOL)blur;

- (void)setBackButtonVisible:(BOOL)visible;

- (void)setBackgroundColor:(UIColor *)backgroundColor;

- (void)setInterceptTouchOutside:(BOOL)interceptTouchOutside;

- (BOOL)isModal;

@end
