#import "RNNBottomTabsPresenter.h"

@implementation RNNBottomTabsPresenter

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent {
    [self setTabBarTranslucent:translucent];
    [self setTabBarBackgroundColor:backgroundColor];
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
    NSLog(@"yooo Value of hello = %@", backgroundColor);
    self.tabBar.barTintColor = backgroundColor;
}

- (void)setTabBarBorderColor:(UIColor *)borderColor {
    NSLog(@"coucou of setTabBarBorderColor RNBottomTAbsPresenter = %@", borderColor);

    self.tabBar.layer.borderColor = (__bridge CGColorRef _Nullable)(borderColor);
    self.tabBar.layer.borderWidth = 2;
}

- (void)setTabBarTranslucent:(BOOL)translucent {
    self.tabBar.translucent = translucent;
}

@end
