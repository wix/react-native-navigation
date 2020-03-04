#import "BottomTabsAppearancePresenter.h"

@implementation BottomTabsAppearancePresenter

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
    UITabBarController *bottomTabs = self.tabBarController;
    for (UIViewController* childViewController in bottomTabs.childViewControllers) {
        childViewController.tabBarItem.standardAppearance.backgroundColor = backgroundColor;
    }
}

@end
