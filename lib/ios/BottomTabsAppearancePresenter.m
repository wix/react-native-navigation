#import "BottomTabsAppearancePresenter.h"
#import "UIColor+RNNUtils.h"
#import "UIImage+Utils.h"

@implementation BottomTabsAppearancePresenter

#pragma mark - public

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent {
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
        for (UIViewController *childViewController in self.tabBarController.childViewControllers)
            childViewController.tabBarItem.standardAppearance.shadowImage = [UIImage
                imageWithSize:CGSizeMake(1.0, [[options.borderWidth withDefault:@(0.1)] floatValue])
                        color:[options.borderColor withDefault:UIColor.blackColor]];
    }
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
    [self setTabBarOpaqueBackground];
    for (UIViewController *childViewController in self.tabBarController.childViewControllers)
        childViewController.tabBarItem.standardAppearance.backgroundColor = backgroundColor;
}

- (void)setTabBarTranslucent:(BOOL)translucent {
    if (translucent)
        [self setTabBarTranslucentBackground];
    else
        [self setTabBarOpaqueBackground];
}

#pragma mark - private

- (void)setTabBarDefaultBackground {
    [self setTabBarOpaqueBackground];
}

- (void)setTabBarTranslucentBackground {
    for (UIViewController *childViewController in self.tabBarController.childViewControllers)
        [childViewController.tabBarItem.standardAppearance configureWithDefaultBackground];
}

- (void)setTabBarTransparentBackground {
    for (UIViewController *childViewController in self.tabBarController.childViewControllers)
        [childViewController.tabBarItem.standardAppearance configureWithTransparentBackground];
}

- (void)setTabBarOpaqueBackground {
    for (UIViewController *childViewController in self.tabBarController.childViewControllers)
        [childViewController.tabBarItem.standardAppearance configureWithOpaqueBackground];
}

@end
