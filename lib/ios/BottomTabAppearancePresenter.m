#import "BottomTabAppearancePresenter.h"
#import "TabBarItemAppearanceCreator.h"

@implementation BottomTabAppearancePresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions children:(NSArray<UIViewController *> *)children {
    self = [super initWithDefaultOptions:defaultOptions];
    for (UIViewController* child in children) {
        child.tabBarItem.standardAppearance = [[UITabBarAppearance alloc] init];
    }
    return self;
}

- (void)createTabBarItem:(UIViewController *)child bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    child.tabBarItem = [TabBarItemAppearanceCreator updateTabBarItem:child.tabBarItem bottomTabOptions:bottomTabOptions];
}

@end
