#import "BottomTabsAppearancePresenter.h"
#import "UIColor+RNNUtils.h"
#import "UIImage+Utils.h"

@implementation BottomTabsAppearancePresenter

#pragma mark - public

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent {
    if (@available(iOS 26.0, *)) {
        if (backgroundColor) {
            if (backgroundColor.isTransparent) {
                [self setTabBarTransparentBackground];
            } else {
                [self setTabBarBackgroundColor:backgroundColor];
            }
        } else {
            [self setTabBarDefaultBackground];
        }
        return;
    }
    if (translucent)
        [self setTabBarTranslucent:YES];
    else if (backgroundColor.isTransparent)
        [self setTabBarTransparentBackground];
    else if (backgroundColor)
        [self setTabBarBackgroundColor:backgroundColor];
    else
        [self setTabBarDefaultBackground];
}

- (void)applyTabBarBorder:(RNNBottomTabsOptions *)options {
    if (options.borderColor.hasValue || options.borderWidth.hasValue) {
        UIImage *borderImage = [UIImage
            imageWithSize:CGSizeMake(1.0, [[options.borderWidth withDefault:@(0.1)] floatValue])
                    color:[options.borderColor withDefault:UIColor.blackColor]];

        for (UIViewController *childViewController in self.tabBarController.childViewControllers) {
            childViewController.tabBarItem.standardAppearance.shadowImage = borderImage;
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 150000
            if (@available(iOS 15.0, *)) {
                childViewController.tabBarItem.scrollEdgeAppearance.shadowImage = borderImage;
            }
#endif
        }
    }
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
    UITabBarAppearance *appearance = [self appearanceWithColor:backgroundColor];
    [self applyTabBarAppearance:appearance];
    self.tabBar.barTintColor = backgroundColor;
    self.tabBar.translucent = NO;
    if (@available(iOS 26.0, *)) {
        self.tabBar.backgroundColor = backgroundColor;
    }
}

- (void)setTabBarTranslucent:(BOOL)translucent {
    self.tabBar.translucent = translucent;
    if (translucent)
        [self setTabBarTranslucentBackground];
    else
        [self setTabBarOpaqueBackground];
}

#pragma mark - private

- (void)setTabBarDefaultBackground {
    if (@available(iOS 26.0, *)) {
        UITabBarAppearance *appearance = [UITabBarAppearance new];
        [appearance configureWithDefaultBackground];
        [self applyTabBarAppearance:appearance];
    } else {
        [self setTabBarOpaqueBackground];
    }
}

- (void)setTabBarTranslucentBackground {
    UITabBarAppearance *appearance = [UITabBarAppearance new];
    [appearance configureWithDefaultBackground];
    [self applyTabBarAppearance:appearance];
    self.tabBar.barTintColor = nil;
}

- (void)setTabBarTransparentBackground {
    UITabBarAppearance *appearance = [UITabBarAppearance new];
    [appearance configureWithTransparentBackground];
    appearance.backgroundEffect = nil;
    appearance.backgroundColor = UIColor.clearColor;
    [self applyTabBarAppearance:appearance];
    self.tabBar.barTintColor = UIColor.clearColor;
}

- (void)setTabBarOpaqueBackground {
    UITabBarAppearance *appearance = [self appearanceWithColor:nil];
    [self applyTabBarAppearance:appearance];
    self.tabBar.barTintColor = UIColor.systemBackgroundColor;
    self.tabBar.translucent = NO;
}

#pragma mark - helpers

- (UITabBarAppearance *)appearanceWithColor:(UIColor *)color {
    UITabBarAppearance *appearance = [UITabBarAppearance new];
    if (@available(iOS 26.0, *)) {
        [appearance configureWithTransparentBackground];
    } else {
        [appearance configureWithOpaqueBackground];
    }
    appearance.backgroundEffect = nil;
    appearance.shadowColor = nil;
    UIColor *resolvedColor = color ?: UIColor.systemBackgroundColor;
    appearance.backgroundColor = resolvedColor;
    appearance.backgroundImage = [UIImage imageWithSize:CGSizeMake(1, 1) color:resolvedColor];
    return appearance;
}

- (void)applyTabBarAppearance:(UITabBarAppearance *)appearance {
    self.tabBar.standardAppearance = appearance;
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 150000
    if (@available(iOS 15.0, *)) {
        self.tabBar.scrollEdgeAppearance = [appearance copy];
    }
#endif
}

@end
