#import "RNNBackButtonOptions.h"
#import "RNNBottomTabOptions.h"
#import "RNNNavigationOptions.h"
#import "UIViewController+RNNOptions.h"
#import <React/RCTRootView.h>

#define kStatusBarAnimationDuration 0.35
const NSInteger BLUR_STATUS_TAG = 78264801;

@implementation UIViewController (RNNOptions)

- (void)setBackgroundImage:(UIImage *)backgroundImage {
    if (backgroundImage) {
        UIImageView *backgroundImageView = self.view.subviews.firstObject;
        if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
            backgroundImageView = [[UIImageView alloc] initWithFrame:self.view.bounds];
            [self.view insertSubview:backgroundImageView atIndex:0];
        }

        backgroundImageView.layer.masksToBounds = YES;
        backgroundImageView.image = backgroundImage;
        [backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
    }
}

- (void)setSearchBarWithOptions:(NSString *)placeholder
                                   focus:(BOOL)focus
                       hideTopBarOnFocus:(BOOL)hideTopBarOnFocus
                            hideOnScroll:(BOOL)hideOnScroll
    obscuresBackgroundDuringPresentation:(BOOL)obscuresBackgroundDuringPresentation
                         backgroundColor:(nullable UIColor *)backgroundColor
                               tintColor:(nullable UIColor *)tintColor
                              cancelText:(NSString *)cancelText {
    if (!self.navigationItem.searchController) {
        UISearchController *search =
            [[UISearchController alloc] initWithSearchResultsController:nil];
        search.dimsBackgroundDuringPresentation = obscuresBackgroundDuringPresentation;
        if ([self conformsToProtocol:@protocol(UISearchResultsUpdating)]) {
            [search setSearchResultsUpdater:((UIViewController<UISearchResultsUpdating> *)self)];
        }
        search.searchBar.delegate = (id<UISearchBarDelegate>)self;
        if (placeholder) {
            search.searchBar.placeholder = placeholder;
        }
        if (cancelText) {
            [search.searchBar setValue:cancelText forKey:@"cancelButtonText"];
        }
        search.hidesNavigationBarDuringPresentation = hideTopBarOnFocus;
        search.searchBar.searchBarStyle = UISearchBarStyleProminent;
        search.searchBar.tintColor = tintColor;
        if (@available(iOS 13.0, *)) {
            search.searchBar.searchTextField.backgroundColor = backgroundColor;
        }

        if (focus) {
            dispatch_async(dispatch_get_main_queue(), ^{
              self.navigationItem.searchController.active = true;
              [self.navigationItem.searchController.searchBar becomeFirstResponder];
            });
        }

        self.navigationItem.searchController = search;
        [self.navigationItem setHidesSearchBarWhenScrolling:hideOnScroll];

        // Fixes #3450, otherwise, UIKit will infer the presentation context to
        // be the root most view controller
        self.definesPresentationContext = YES;
    }
}

- (void)setSearchBarHiddenWhenScrolling:(BOOL)searchBarHidden {
    self.navigationItem.hidesSearchBarWhenScrolling = searchBarHidden;
}

- (void)setSearchBarVisible:(BOOL)visible {
    if (!visible) {
        self.navigationItem.searchController = nil;
    }
}

- (void)setNavigationItemTitle:(NSString *)title {
    self.navigationItem.title = title;
}

- (void)setTabBarItemBadge:(NSString *)badge {
    UITabBarItem *tabBarItem = self.tabBarItem;

    if ([badge isKindOfClass:[NSNull class]] || [badge isEqualToString:@""]) {
        tabBarItem.badgeValue = nil;
    } else {
        tabBarItem.badgeValue = badge;
        [[self.tabBarController.tabBar viewWithTag:tabBarItem.tag] removeFromSuperview];
        tabBarItem.tag = -1;
    }
}

- (void)setTabBarItemBadgeColor:(UIColor *)badgeColor {
    if (@available(iOS 13.0, *)) {
        self.tabBarItem.standardAppearance.stackedLayoutAppearance.normal.badgeBackgroundColor =
            badgeColor;

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 150000
        if (@available(iOS 15.0, *)) {
            self.tabBarItem.scrollEdgeAppearance.stackedLayoutAppearance.normal
                .badgeBackgroundColor = badgeColor;
        }
#endif
    } else {
        self.tabBarItem.badgeColor = badgeColor;
    }
}

- (void)setStatusBarStyle:(NSString *)style animated:(BOOL)animated {
    if (animated) {
        [UIView animateWithDuration:[self statusBarAnimationDuration:animated]
                         animations:^{
                           [self setNeedsStatusBarAppearanceUpdate];
                         }];
    } else {
        [self setNeedsStatusBarAppearanceUpdate];
    }
}

- (void)setTopBarPrefersLargeTitle:(BOOL)prefersLargeTitle {
    if (prefersLargeTitle) {
        self.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
    } else {
        self.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeNever;
    }
}

- (void)setStatusBarBlur:(BOOL)blur {
    UIView *curBlurView = [self.view viewWithTag:BLUR_STATUS_TAG];
    if (blur) {
        if (!curBlurView) {
            UIVisualEffectView *blur = [[UIVisualEffectView alloc]
                initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleRegular]];
            blur.frame = [[UIApplication sharedApplication] statusBarFrame];
            blur.tag = BLUR_STATUS_TAG;
            [self.view addSubview:blur];
        }
    } else {
        if (curBlurView) {
            [curBlurView removeFromSuperview];
        }
    }
}

- (void)setBackgroundColor:(UIColor *)backgroundColor {
    self.view.backgroundColor = backgroundColor;
}

- (void)setBackButtonVisible:(BOOL)visible {
    self.navigationItem.hidesBackButton = !visible;
}

- (CGFloat)statusBarAnimationDuration:(BOOL)animated {
    return animated ? kStatusBarAnimationDuration : CGFLOAT_MIN;
}

- (BOOL)isModal {
    if ([self presentingViewController])
        return YES;
    if ([[[self navigationController] presentingViewController] presentedViewController] ==
        [self navigationController])
        return YES;
    if ([[[self tabBarController] presentingViewController]
            isKindOfClass:[UITabBarController class]])
        return YES;

    return NO;
}

@end
