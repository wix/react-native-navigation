#import "TabBarItemIOS15Creator.h"

@implementation TabBarItemIOS15Creator

#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 150000

- (UITabBarItem *)createTabBarItem:(UITabBarItem *)mergeItem {
    UITabBarItem *tabBarItem = [super createTabBarItem:mergeItem];
    tabBarItem.scrollEdgeAppearance =
        mergeItem.scrollEdgeAppearance ?: [[UITabBarAppearance alloc] init];
    return tabBarItem;
}

- (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
    [super setTitleAttributes:tabItem titleAttributes:titleAttributes];
    tabItem.scrollEdgeAppearance.stackedLayoutAppearance.normal.titleTextAttributes =
        titleAttributes;
    tabItem.scrollEdgeAppearance.compactInlineLayoutAppearance.normal.titleTextAttributes =
        titleAttributes;
    tabItem.scrollEdgeAppearance.inlineLayoutAppearance.normal.titleTextAttributes =
        titleAttributes;
}

- (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem
           selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
    [super setSelectedTitleAttributes:tabItem selectedTitleAttributes:selectedTitleAttributes];
    tabItem.scrollEdgeAppearance.stackedLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
    tabItem.scrollEdgeAppearance.compactInlineLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
    tabItem.scrollEdgeAppearance.inlineLayoutAppearance.selected.titleTextAttributes =
        selectedTitleAttributes;
}

#endif

@end
