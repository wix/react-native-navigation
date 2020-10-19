#import "TabBarItemAppearanceCreator.h"

@implementation TabBarItemAppearanceCreator

+ (UITabBarItem *)createTabBarItem:(UITabBarItem *)mergeItem {
    UITabBarItem* tabBarItem = [super createTabBarItem:mergeItem];
    tabBarItem.standardAppearance = mergeItem.standardAppearance ?: [[UITabBarAppearance alloc] init];
    return tabBarItem;
}

+ (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
    [super setTitleAttributes:tabItem titleAttributes:titleAttributes];
    tabItem.standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = titleAttributes;
    tabItem.standardAppearance.compactInlineLayoutAppearance.normal.titleTextAttributes = titleAttributes;
    tabItem.standardAppearance.inlineLayoutAppearance.normal.titleTextAttributes = titleAttributes;
    if (@available(iOS 14.0, *)) {
      UIColor *color = [titleAttributes valueForKey:NSForegroundColorAttributeName];
      if (color) {
        tabItem.standardAppearance.inlineLayoutAppearance.normal.titleTextAttributes = @{NSForegroundColorAttributeName: color};
      }
    }
}

+ (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
    [super setSelectedTitleAttributes:tabItem selectedTitleAttributes:selectedTitleAttributes];
    tabItem.standardAppearance.stackedLayoutAppearance.selected.titleTextAttributes = selectedTitleAttributes;
    tabItem.standardAppearance.compactInlineLayoutAppearance.selected.titleTextAttributes = selectedTitleAttributes;
    tabItem.standardAppearance.inlineLayoutAppearance.selected.titleTextAttributes = selectedTitleAttributes;
    if (@available(iOS 14.0, *)) {
      UIColor *color = [selectedTitleAttributes valueForKey:NSForegroundColorAttributeName];
      if (color) {
        tabItem.standardAppearance.inlineLayoutAppearance.selected.titleTextAttributes = @{NSForegroundColorAttributeName: color};
      }
    }
}

@end
