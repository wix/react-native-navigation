#import "BottomTabAppearancePresenter.h"
#import "TabBarItemAppearanceCreator.h"

@implementation BottomTabAppearancePresenter

- (void)bindViewController:(UIViewController *)boundViewController {
    [super bindViewController:boundViewController];
    boundViewController.tabBarItem.standardAppearance = [[UITabBarAppearance alloc] init];
}

- (void)updateTabBarItem:(UITabBarItem *)tabItem bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    self.boundViewController.tabBarItem = [TabBarItemAppearanceCreator updateTabBarItem:self.boundViewController.tabBarItem bottomTabOptions:bottomTabOptions];
}

@end
