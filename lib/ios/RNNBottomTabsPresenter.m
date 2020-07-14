#import "RNNBottomTabsPresenter.h"

@implementation RNNBottomTabsPresenter

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent {
    [self setTabBarTranslucent:translucent];
    [self setTabBarBackgroundColor:backgroundColor];
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
    self.tabBar.barTintColor = backgroundColor;
}

- (void)setTabBarTranslucent:(BOOL)translucent {
    self.tabBar.translucent = translucent;
}

@end
