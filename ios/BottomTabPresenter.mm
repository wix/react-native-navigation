#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"

@implementation BottomTabPresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions
                            tabCreator:(RNNTabBarItemCreator *)tabCreator {
    self = [super initWithDefaultOptions:defaultOptions];
    _tabCreator = tabCreator;
    return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = [options withDefault:self.defaultOptions];

    [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    [child setTabBarItemBadge:[withDefault.bottomTab.badge withDefault:[NSNull null]]];
    [child setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor withDefault:nil]];
}

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions
               child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[resolvedOptions
        withDefault:self.defaultOptions] mergeOptions:mergeOptions];

    if (mergeOptions.bottomTab.hasValue)
        [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    if (mergeOptions.bottomTab.badge.hasValue)
        [child setTabBarItemBadge:mergeOptions.bottomTab.badge.get];
    if (mergeOptions.bottomTab.badgeColor.hasValue)
        [child setTabBarItemBadgeColor:mergeOptions.bottomTab.badgeColor.get];
}

- (void)createTabBarItem:(UIViewController *)child
        bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    if (_useCustomItemViews && bottomTabOptions.component.name.hasValue) {
        UITabBarItem *blankItem = [self createBlankTabBarItem:child
                                             bottomTabOptions:bottomTabOptions];
        if (blankItem != child.tabBarItem) {
            child.tabBarItem = blankItem;
        }
        return;
    }

    UITabBarItem *updatedItem = [_tabCreator createTabBarItem:bottomTabOptions mergeItem:child.tabBarItem];
    if (updatedItem != child.tabBarItem) {
        child.tabBarItem = updatedItem;
    }
}

// Builds a truly blank `UITabBarItem` (nil image, nil title). When custom
// item views are active, `RNNBottomTabsController` hides the native tab bar
// visuals and renders the custom row on top. The bar item still needs to
// exist so that `UITabBarController` reserves the right number of slots and
// the bottom safe-area inset.
- (UITabBarItem *)createBlankTabBarItem:(UIViewController *)child
                       bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    UITabBarItem *item = child.tabBarItem ?: [UITabBarItem new];
    item.image = nil;
    item.selectedImage = nil;
    item.title = nil;
    item.tag = bottomTabOptions.tag;
    item.accessibilityIdentifier = [bottomTabOptions.testID withDefault:nil];
    item.accessibilityLabel = [bottomTabOptions.accessibilityLabel withDefault:nil];
    item.imageInsets = UIEdgeInsetsZero;
    return item;
}

@end
