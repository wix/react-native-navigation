#import "RNNTabBarItemCreator.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+utils.h"
#import <React/RCTLog.h>

@implementation RNNTabBarItemCreator

+ (NSNumber *)systemItemForRole:(NSString *)role {
    static NSDictionary<NSString *, NSNumber *> *map = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        map = @{
            @"search"     : @(UITabBarSystemItemSearch),
            @"bookmarks"  : @(UITabBarSystemItemBookmarks),
            @"contacts"   : @(UITabBarSystemItemContacts),
            @"downloads"  : @(UITabBarSystemItemDownloads),
            @"favorites"  : @(UITabBarSystemItemFavorites),
            @"featured"   : @(UITabBarSystemItemFeatured),
            @"history"    : @(UITabBarSystemItemHistory),
            @"more"       : @(UITabBarSystemItemMore),
            @"mostRecent" : @(UITabBarSystemItemMostRecent),
            @"mostViewed" : @(UITabBarSystemItemMostViewed),
            @"recents"    : @(UITabBarSystemItemRecents),
            @"topRated"   : @(UITabBarSystemItemTopRated),
        };
    });
    return map[role];
}

- (UITabBarItem *)createTabBarItem:(UITabBarItem *)mergeItem {
    return mergeItem ?: [UITabBarItem new];
}

- (UITabBarItem *)createTabBarItem:(RNNBottomTabOptions *)bottomTabOptions
                         mergeItem:(UITabBarItem *)mergeItem {

    if (bottomTabOptions.role.hasValue) {
        UITabBarItem *roleItem = [self createSystemItemForRole:bottomTabOptions
                                                     mergeItem:mergeItem];
        if (roleItem)
            return roleItem;
    }

    UITabBarItem *tabItem = [self createTabBarItem:mergeItem];
    UIImage *icon = [bottomTabOptions.icon withDefault:nil];
    UIImage *selectedIcon = [bottomTabOptions.selectedIcon withDefault:icon];
    UIColor *iconColor = [bottomTabOptions.iconColor withDefault:nil];
    UIColor *selectedIconColor = [bottomTabOptions.selectedIconColor withDefault:iconColor];

    if (@available(iOS 13.0, *)) {
        if (bottomTabOptions.sfSymbol.hasValue) {
            icon = [UIImage systemImageNamed:[bottomTabOptions.sfSymbol withDefault:nil]];
        }

        if (bottomTabOptions.sfSelectedSymbol.hasValue) {
            selectedIcon =
                [UIImage systemImageNamed:[bottomTabOptions.sfSelectedSymbol withDefault:nil]];
        }
    }

    tabItem.image = [self getIconImage:icon withTint:iconColor];
    tabItem.selectedImage = [self getSelectedIconImage:selectedIcon
                                     selectedIconColor:selectedIconColor];
    tabItem.title = [bottomTabOptions.text withDefault:nil];
    tabItem.tag = bottomTabOptions.tag;
    tabItem.accessibilityIdentifier = [bottomTabOptions.testID withDefault:nil];
    tabItem.accessibilityLabel = [bottomTabOptions.accessibilityLabel withDefault:nil];

    NSDictionary *iconInsets = [bottomTabOptions.iconInsets withDefault:nil];
    if (iconInsets && ![iconInsets isKindOfClass:[NSNull class]]) {
        id topInset = iconInsets[@"top"];
        id leftInset = iconInsets[@"left"];
        id bottomInset = iconInsets[@"bottom"];
        id rightInset = iconInsets[@"right"];

        CGFloat top = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:topInset] : 0;
        CGFloat left = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:leftInset] : 0;
        CGFloat bottom = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:bottomInset] : 0;
        CGFloat right = topInset != (id)[NSNull null] ? [RCTConvert CGFloat:rightInset] : 0;

        tabItem.imageInsets = UIEdgeInsetsMake(top, left, bottom, right);
    }

    [self appendTitleAttributes:tabItem bottomTabOptions:bottomTabOptions];

    return tabItem;
}

#pragma mark - Role (system item) creation

- (UITabBarItem *)createSystemItemForRole:(RNNBottomTabOptions *)bottomTabOptions
                                mergeItem:(UITabBarItem *)mergeItem {
    NSString *role = [bottomTabOptions.role withDefault:nil];
    NSNumber *systemItemNumber = [RNNTabBarItemCreator systemItemForRole:role];

    if (!systemItemNumber) {
        RCTLogWarn(@"[RNN] Unknown bottomTab role '%@' — falling back to normal tab.", role);
        return nil;
    }

    if ([role isEqualToString:@"search"] && self.searchRoleUsed) {
        RCTLogWarn(@"[RNN] Only one tab per bottomTabs layout may use role:'search'. "
                   @"Subsequent 'search' roles are ignored — creating a normal tab instead.");
        return nil;
    }

    if ([role isEqualToString:@"search"]) {
        self.searchRoleUsed = YES;
    }

    UITabBarItem *tabItem =
        [[UITabBarItem alloc] initWithTabBarSystemItem:(UITabBarSystemItem)[systemItemNumber integerValue]
                                                  tag:bottomTabOptions.tag];

    UITabBarItem *baseItem = [self createTabBarItem:mergeItem];
    tabItem.standardAppearance = baseItem.standardAppearance;
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 150000
    if (@available(iOS 15.0, *)) {
        tabItem.scrollEdgeAppearance = baseItem.scrollEdgeAppearance;
    }
#endif

    UIImage *icon = [bottomTabOptions.icon withDefault:nil];
    UIImage *selectedIcon = [bottomTabOptions.selectedIcon withDefault:nil];
    UIColor *iconColor = [bottomTabOptions.iconColor withDefault:nil];
    UIColor *selectedIconColor = [bottomTabOptions.selectedIconColor withDefault:iconColor];

    if (@available(iOS 13.0, *)) {
        if (bottomTabOptions.sfSymbol.hasValue) {
            icon = [UIImage systemImageNamed:[bottomTabOptions.sfSymbol withDefault:nil]];
        }
        if (bottomTabOptions.sfSelectedSymbol.hasValue) {
            selectedIcon = [UIImage systemImageNamed:[bottomTabOptions.sfSelectedSymbol withDefault:nil]];
        }
    }

    if (icon) {
        tabItem.image = [self getIconImage:icon withTint:iconColor];
    }
    if (selectedIcon) {
        tabItem.selectedImage = [self getSelectedIconImage:selectedIcon
                                         selectedIconColor:selectedIconColor];
    }

    tabItem.accessibilityIdentifier = [bottomTabOptions.testID withDefault:nil];
    tabItem.accessibilityLabel = [bottomTabOptions.accessibilityLabel withDefault:nil];
    [self appendTitleAttributes:tabItem bottomTabOptions:bottomTabOptions];

    return tabItem;
}

#pragma mark - Icon helpers

- (UIImage *)getSelectedIconImage:(UIImage *)selectedIcon
                selectedIconColor:(UIColor *)selectedIconColor {
    if (selectedIcon) {
        if (selectedIconColor) {
            return [[selectedIcon withTintColor:selectedIconColor]
                imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        } else {
            return [selectedIcon imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        }
    }

    return nil;
}

- (UIImage *)getIconImage:(UIImage *)icon withTint:(UIColor *)tintColor {
    if (icon) {
        if (tintColor) {
            return [[icon withTintColor:tintColor]
                imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        } else {
            return [icon imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
        }
    }

    return nil;
}

#pragma mark - Title attributes

- (void)appendTitleAttributes:(UITabBarItem *)tabItem
             bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    UIColor *textColor = [bottomTabOptions.textColor withDefault:[UIColor blackColor]];
    UIColor *selectedTextColor =
        [bottomTabOptions.selectedTextColor withDefault:[UIColor blackColor]];
    NSString *fontFamily = [bottomTabOptions.fontFamily withDefault:nil];
    NSNumber *fontSize = [bottomTabOptions.fontSize withDefault:@(10)];
    NSString *fontWeight = [bottomTabOptions.fontWeight withDefault:nil];

    NSDictionary *normalAttributes = [RNNFontAttributesCreator
        createFromDictionary:[tabItem titleTextAttributesForState:UIControlStateNormal]
                  fontFamily:fontFamily
                    fontSize:fontSize
                  fontWeight:fontWeight
                       color:textColor
                    centered:YES];
    [self setTitleAttributes:tabItem titleAttributes:normalAttributes];

    NSDictionary *selectedAttributes = [RNNFontAttributesCreator
        createFromDictionary:[tabItem titleTextAttributesForState:UIControlStateSelected]
                  fontFamily:fontFamily
                    fontSize:fontSize
                  fontWeight:fontWeight
                       color:selectedTextColor
                    centered:YES];
    [self setSelectedTitleAttributes:tabItem selectedTitleAttributes:selectedAttributes];
}

- (void)setTitleAttributes:(UITabBarItem *)tabItem titleAttributes:(NSDictionary *)titleAttributes {
    [tabItem setTitleTextAttributes:titleAttributes forState:UIControlStateNormal];
}

- (void)setSelectedTitleAttributes:(UITabBarItem *)tabItem
           selectedTitleAttributes:(NSDictionary *)selectedTitleAttributes {
    [tabItem setTitleTextAttributes:selectedTitleAttributes forState:UIControlStateSelected];
}

@end
