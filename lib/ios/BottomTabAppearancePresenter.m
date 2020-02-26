#import "BottomTabAppearancePresenter.h"
#import "TabBarItemAppearanceCreator.h"

@implementation BottomTabAppearancePresenter

- (void)bindViewController:(UIViewController *)boundViewController {
    [super bindViewController:boundViewController];
    boundViewController.tabBarItem.standardAppearance = [[UITabBarAppearance alloc] init];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options child:(UIViewController *)child {
    child.tabBarItem.standardAppearance = [[UITabBarAppearance alloc] init];
    [super applyOptionsOnWillMoveToParentViewController:options child:child];
}

- (void)updateChild:(UIViewController *)child bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    child.tabBarItem = [TabBarItemAppearanceCreator updateTabBarItem:child.tabBarItem bottomTabOptions:bottomTabOptions];
}

@end
