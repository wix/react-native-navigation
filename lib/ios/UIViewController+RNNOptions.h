#import <UIKit/UIKit.h>

@class RNNBottomTabOptions;
@class RNNNavigationOptions;
@class RNNBackButtonOptions;

@interface UIViewController (RNNOptions)

- (void)setBackgroundImage:(UIImage *_Nullable)backgroundImage;

- (void)setSearchBarWithPlaceholder:(NSString *_Nullable)placeholder
                       hideTopBarOnFocus:(BOOL)hideNavBarOnFocusSearchBar
                            hideOnScroll:(BOOL)searchBarHiddenWhenScrolling
    obscuresBackgroundDuringPresentation:(BOOL)obscuresBackgroundDuringPresentation
                         backgroundColor:(nullable UIColor *)backgroundColor
                               tintColor:(nullable UIColor *)tintColor;

- (void)setSearchBarHiddenWhenScrolling:(BOOL)searchBarHidden;

- (void)setTabBarItemBadgeColor:(UIColor *_Nullable)badgeColor;

- (void)setTabBarItemBadge:(NSString *_Nullable)badge;

- (void)setTopBarPrefersLargeTitle:(BOOL)prefersLargeTitle;

- (void)setNavigationItemTitle:(NSString *_Nullable)title;

- (void)setStatusBarStyle:(NSString *_Nullable)style animated:(BOOL)animated;

- (void)setStatusBarBlur:(BOOL)blur;

- (void)setBackButtonVisible:(BOOL)visible;

- (void)setBackgroundColor:(UIColor *_Nullable)backgroundColor;

- (BOOL)isModal;

@end
