#import "TabBarItemAppearanceCreator.h"

@implementation TabBarItemAppearanceCreator

+ (UITabBarItem *)createTabBarItem:(UITabBarItem *)mergeItem {
    UITabBarItem *tabBarItem = [super createTabBarItem:mergeItem];
    tabBarItem.standardAppearance =
        mergeItem.standardAppearance ?: [[UITabBarAppearance alloc] init];
    if (@available(iOS 15.0, *)) {
        tabBarItem.scrollEdgeAppearance =
            mergeItem.scrollEdgeAppearance ?: [[UITabBarAppearance alloc] init];
    }
    return tabBarItem;
}

+ (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = titleAttributes;
    tabItem.standardAppearance.compactInlineLayoutAppearance.normal.titleTextAttributes =
        titleAttributes;
    tabItem.standardAppearance.inlineLayoutAppearance.normal.titleTextAttributes = titleAttributes;
    if (@available(iOS 15.0, *)) {
        tabItem.scrollEdgeAppearance.stackedLayoutAppearance.normal.titleTextAttributes = titleAttributes;
        tabItem.scrollEdgeAppearance.compactInlineLayoutAppearance.normal.titleTextAttributes =
            titleAttributes;
        tabItem.scrollEdgeAppearance.inlineLayoutAppearance.normal.titleTextAttributes = titleAttributes;
    }
}

+ (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem
           selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
    tabItem.standardAppearance.compactInlineLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
    tabItem.standardAppearance.inlineLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
    if (@available(iOS 15.0, *)) {
        tabItem.scrollEdgeAppearance.stackedLayoutAppearance.selected.titleTextAttributes =
            selectedTitleAttributes;
        tabItem.scrollEdgeAppearance.compactInlineLayoutAppearance.selected.titleTextAttributes =
            selectedTitleAttributes;
        tabItem.scrollEdgeAppearance.inlineLayoutAppearance.selected.titleTextAttributes =
            selectedTitleAttributes;
    }
}

@end
