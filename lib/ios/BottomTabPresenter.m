#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"

@implementation BottomTabPresenter

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = [options withDefault:self.defaultOptions];

    [child setTabBarItemBadge:[withDefault.bottomTab.badge.text withDefault:[NSNull null]]];
    [child setTabBarItemBadgeColor:[withDefault.bottomTab.badge.backgroundColor withDefault:nil]];
    [child setTabBarItemBadgeTextColor:[withDefault.bottomTab.badge.textColor
                                           withDefault:[UIColor whiteColor]]];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options
                                               child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = [options withDefault:self.defaultOptions];

    [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    [child setTabBarItemBadge:[withDefault.bottomTab.badge.text withDefault:[NSNull null]]];
    [child setTabBarItemBadgeColor:[withDefault.bottomTab.badge.backgroundColor withDefault:nil]];
    [child setTabBarItemBadgeTextColor:[withDefault.bottomTab.badge.textColor
                                           withDefault:[UIColor blackColor]]];
}

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions
               child:(UIViewController *)child {
    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[resolvedOptions
        withDefault:self.defaultOptions] mergeOptions:mergeOptions];

    if (mergeOptions.bottomTab.hasValue)
        [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    if (mergeOptions.bottomTab.badge.text.hasValue)
        [child setTabBarItemBadge:mergeOptions.bottomTab.badge.text.get];
    if (mergeOptions.bottomTab.badge.backgroundColor.hasValue)
        [child setTabBarItemBadgeColor:mergeOptions.bottomTab.badge.backgroundColor.get];
    if (mergeOptions.bottomTab.badge.textColor.hasValue)
        [child setTabBarItemBadgeTextColor:[mergeOptions.bottomTab.badge.textColor
                                               withDefault:[UIColor blackColor]]];
}

- (void)createTabBarItem:(UIViewController *)child
        bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    child.tabBarItem = [RNNTabBarItemCreator createTabBarItem:bottomTabOptions
                                                    mergeItem:child.tabBarItem];
}

@end
