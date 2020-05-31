#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"

@implementation BottomTabPresenter

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];
    
    [child setTabBarItemBadge:[withDefault.bottomTab.badge getWithDefaultValue:[NSNull null]]];
    [child setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor getWithDefaultValue:nil]];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options  child:(UIViewController *)child {
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];
    
    [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    [child setTabBarItemBadge:[withDefault.bottomTab.badge getWithDefaultValue:[NSNull null]]];
    [child setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor getWithDefaultValue:nil]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions child:(UIViewController *)child {
    RNNNavigationOptions* withDefault = (RNNNavigationOptions *) [[resolvedOptions withDefault:self.defaultOptions] overrideOptions:options];
    
    if (options.bottomTab.hasValue) [self createTabBarItem:child bottomTabOptions:withDefault.bottomTab];
    if (options.bottomTab.badge.hasValue) [child setTabBarItemBadge:options.bottomTab.badge.get];
    if (options.bottomTab.badgeColor.hasValue) [child setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
}

- (void)createTabBarItem:(UIViewController *)child bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    child.tabBarItem = [RNNTabBarItemCreator createTabBarItem:bottomTabOptions mergeItem:child.tabBarItem];
}

@end
