#import "TabBarItemAppearanceCreator.h"

@implementation TabBarItemAppearanceCreator

+ (UITabBarItem *)createTabBarItem {
    UITabBarItem* tabBarItem = [super createTabBarItem];
    tabBarItem.standardAppearance = [[UITabBarAppearance alloc] init];
    return tabBarItem;
}

+ (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = titleAttributes;
}

+ (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.selected.titleTextAttributes = selectedTitleAttributes;
}

@end
