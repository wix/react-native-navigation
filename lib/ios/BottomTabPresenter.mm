#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"

@implementation BottomTabPresenter {
    RNNTabBarItemCreator *_tabCreator;
}

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
    child.tabBarItem = [_tabCreator createTabBarItem:bottomTabOptions mergeItem:child.tabBarItem];
}

@end
