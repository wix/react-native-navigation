#import "BottomTabAppearancePresenter.h"
#import "TabBarItemAppearanceCreator.h"

@implementation BottomTabAppearancePresenter

- (void)createTabBarItem:(UIViewController *)child bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    child.tabBarItem = [TabBarItemAppearanceCreator createTabBarItem:bottomTabOptions mergeItem:child.tabBarItem];
}

@end
