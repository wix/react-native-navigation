#import "BottomTabAppearancePresenter.h"
#import "TabBarItemAppearanceCreator.h"

@implementation BottomTabAppearancePresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super initWithDefaultOptions:defaultOptions];
    return self;
}

- (void)bindViewController:(UIViewController *)boundViewController {
    [super bindViewController:boundViewController];
    boundViewController.tabBarItem.standardAppearance = [[UITabBarAppearance alloc] init];
}

- (void)updateTabBarItem:(UITabBarItem *)tabItem bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    UIViewController *viewController = self.boundViewController;
    viewController.tabBarItem = [TabBarItemAppearanceCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:bottomTabOptions];
}

@end
