#import "TabBarItemAppearanceCreator.h"

@implementation TabBarItemAppearanceCreator

- (UITabBarItem *)createTabBarItem:(UITabBarItem *)mergeItem {
    UITabBarItem *tabBarItem = [super createTabBarItem:mergeItem];
    tabBarItem.standardAppearance =
        mergeItem.standardAppearance ?: [[UITabBarAppearance alloc] init];
    return tabBarItem;
}

- (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = titleAttributes;
    tabItem.standardAppearance.compactInlineLayoutAppearance.normal.titleTextAttributes =
        titleAttributes;
    tabItem.standardAppearance.inlineLayoutAppearance.normal.titleTextAttributes = titleAttributes;
}

- (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem
           selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
    tabItem.standardAppearance.compactInlineLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
    tabItem.standardAppearance.inlineLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
}

@end
