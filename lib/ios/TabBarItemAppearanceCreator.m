#import "TabBarItemAppearanceCreator.h"

@implementation TabBarItemAppearanceCreator

+ (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.normal.titleTextAttributes = titleAttributes;
}

+ (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
    tabItem.standardAppearance.stackedLayoutAppearance.selected.titleTextAttributes = selectedTitleAttributes;
}

@end
