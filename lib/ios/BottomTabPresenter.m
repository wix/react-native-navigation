#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+RNNOptions.h"
#import "RNNDotIndicatorPresenter.h"

@interface BottomTabPresenter ()
@property(nonatomic, strong) RNNDotIndicatorPresenter* dotIndicatorPresenter;
@end

@implementation BottomTabPresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super init];
    self.defaultOptions = defaultOptions;
    self.dotIndicatorPresenter = [[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:defaultOptions];
	return self;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];

    if (withDefault.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController setTabBarItemBadge:withDefault.bottomTab.badge.get];
    }

    if (withDefault.bottomTab.badgeColor.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController setTabBarItemBadgeColor:withDefault.bottomTab.badgeColor.get];
    }
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
	UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];

    if (withDefault.bottomTab.text.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (withDefault.bottomTab.icon.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (withDefault.bottomTab.selectedIcon.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (withDefault.bottomTab.badgeColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (withDefault.bottomTab.textColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (withDefault.bottomTab.iconColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (withDefault.bottomTab.selectedTextColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (withDefault.bottomTab.selectedIconColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions* withDefault = (RNNNavigationOptions *) [[resolvedOptions withDefault:self.defaultOptions] overrideOptions:options];
    
    if (options.bottomTab.badge.hasValue) {
        [viewController setTabBarItemBadge:options.bottomTab.badge.get];
    }

    if (options.bottomTab.badgeColor.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
    }

    if (options.bottomTab.text.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if ([options.bottomTab.dotIndicator hasValue] && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [[self dotIndicatorPresenter] apply:viewController:options.bottomTab.dotIndicator];
    }
    
    if (options.bottomTab.icon.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedIcon.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.textColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedTextColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.iconColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedIconColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
        viewController.tabBarItem = tabItem;
    }
}

- (void)applyDotIndicator:(UIViewController *)child {
    
}

@end
