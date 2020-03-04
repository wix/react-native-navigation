#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"

@implementation BottomTabPresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    self.defaultOptions = defaultOptions;
    return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];
    
    if (withDefault.bottomTab.badge.hasValue)
        [child setTabBarItemBadge:withDefault.bottomTab.badge.get];
    
    if (withDefault.bottomTab.badgeColor.hasValue)
        [child setTabBarItemBadgeColor:withDefault.bottomTab.badgeColor.get];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options  child:(UIViewController *)child {
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];
    
    [child setTabBarItemBadge:[withDefault.bottomTab.badge getWithDefaultValue:nil]];
    [child setTabBarItemBadgeColor:[withDefault.bottomTab.badgeColor getWithDefaultValue:nil]];
    [self updateChild:child bottomTabOptions:withDefault.bottomTab];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions child:(UIViewController *)child {
    RNNNavigationOptions* withDefault = (RNNNavigationOptions *) [[resolvedOptions withDefault:self.defaultOptions] overrideOptions:options];
    
    if (options.bottomTab.badge.hasValue)
        [child setTabBarItemBadge:options.bottomTab.badge.get];
    
    if (options.bottomTab.badgeColor.hasValue)
        [child setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
    
    if (options.bottomTab.hasValue)
        [self updateChild:child bottomTabOptions:withDefault.bottomTab];
}

- (void)updateChild:(UIViewController *)child bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    child.tabBarItem = [RNNTabBarItemCreator updateTabBarItem:child.tabBarItem bottomTabOptions:bottomTabOptions];
}

@end
