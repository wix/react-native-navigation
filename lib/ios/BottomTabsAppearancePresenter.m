#import "BottomTabsAppearancePresenter.h"
#import "UIColor+RNNUtils.h"

@implementation BottomTabsAppearancePresenter

# pragma mark - public

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent {
    if (translucent) [self setTabBarTranslucent:YES];
    else if (backgroundColor.isTransparent) [self setTabBarTransparentBackground];
    else if (backgroundColor) [self setTabBarBackgroundColor:backgroundColor];
    else [self setTabBarDefaultBackground];
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
    [self setTabBarOpaqueBackground];
    for (UIViewController* childViewController in self.tabBarController.childViewControllers)
        childViewController.tabBarItem.standardAppearance.backgroundColor = backgroundColor;
}

- (void)setTabBarTranslucent:(BOOL)translucent {
    if (translucent) [self setTabBarTranslucentBackground];
    else [self setTabBarOpaqueBackground];
}

# pragma mark - private

- (void)setTabBarDefaultBackground {
    [self setTabBarOpaqueBackground];
}

- (void)setTabBarTranslucentBackground {
    for (UIViewController* childViewController in self.tabBarController.childViewControllers)
        [childViewController.tabBarItem.standardAppearance configureWithDefaultBackground];
}

- (void)setTabBarTransparentBackground {
    for (UIViewController* childViewController in self.tabBarController.childViewControllers)
        [childViewController.tabBarItem.standardAppearance configureWithTransparentBackground];
}

- (void)setTabBarOpaqueBackground {
    for (UIViewController* childViewController in self.tabBarController.childViewControllers)
        [childViewController.tabBarItem.standardAppearance configureWithOpaqueBackground];
}

@end
