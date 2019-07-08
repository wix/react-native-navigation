#import "RNNBasePresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNTabBarItemCreator.h"
#import "RNNReactComponentRegistry.h"
#import "UIViewController+LayoutProtocol.h"
#import "UITabBar+Utils.h"
#import "UIView+Utils.h"

@interface RNNBasePresenter ()
@end


@implementation RNNBasePresenter

- (void)bindViewController:(UIViewController <RNNLayoutProtocol> *)bindedViewController {
    self.bindedComponentId = bindedViewController.layoutInfo.componentId;
    _boundViewController = bindedViewController;
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {

}

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options {

}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
    UIViewController *viewController = self.boundViewController;

    if (options.bottomTab.text.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.icon.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedIcon.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.badgeColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.textColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.iconColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedTextColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedIconColor.hasValue) {
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
        viewController.tabBarItem = tabItem;
    }
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    UIViewController *viewController = self.boundViewController;

    if (options.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController rnn_setTabBarItemBadge:options.bottomTab];
    }

    if (options.bottomTab.badgeColor.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController rnn_setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
    }
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
    UIViewController *viewController = self.boundViewController;
    if (newOptions.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController rnn_setTabBarItemBadge:newOptions.bottomTab];
    }

    if (newOptions.bottomTab.badgeColor.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController rnn_setTabBarItemBadgeColor:newOptions.bottomTab.badgeColor.get];
    }

    if (newOptions.bottomTab.badgeSize.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [self applyBadgeSize:viewController];
    }

    if (newOptions.bottomTab.text.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = [(RNNNavigationOptions *) [currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (newOptions.bottomTab.icon.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = [(RNNNavigationOptions *) [currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (newOptions.bottomTab.selectedIcon.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = [(RNNNavigationOptions *) [currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (newOptions.bottomTab.textColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = [(RNNNavigationOptions *) [currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (newOptions.bottomTab.selectedTextColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = [(RNNNavigationOptions *) [currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (newOptions.bottomTab.iconColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = [(RNNNavigationOptions *) [currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (newOptions.bottomTab.selectedIconColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = [(RNNNavigationOptions *) [currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }
}

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    if (readyBlock) {
        readyBlock();
        readyBlock = nil;
    }
}

- (void)viewDidLayoutSubviews {

}

- (void)applyBadgeSize:(UIViewController *)child {
    RNNNavigationOptions *options = [[self boundViewController] resolveChildOptions:child];
    if (![options.bottomTab.badgeSize hasValue]) return;

    if (options.bottomTab.badgeSize.get.floatValue == 0 && [child tabBarItem].tag > 0) {
        [[[[[self boundViewController] tabBarController] tabBar] viewWithTag:[child tabBarItem].tag] removeFromSuperview];
        [child tabBarItem].tag = -1;
        return;
    }

    if ([child tabBarItem].tag > 0) return;

    UITabBarController *bottomTabs = [self getTabBarController];
    int index = (int) [[bottomTabs childViewControllers] indexOfObject:child];
    UITabBar *tabBar = [bottomTabs tabBar];
    UIView *icon = [tabBar getTabIcon:index];

    float size = [options.bottomTab.badgeSize.get floatValue];
    UIView *badge = [UIView new];
    badge.translatesAutoresizingMaskIntoConstraints = NO;

    badge.layer.cornerRadius = size / 2;
    badge.backgroundColor = UIColor.redColor;
    badge.tag = arc4random();

    [child tabBarItem].tag = badge.tag;
    [child.tabBarController.tabBar addSubview:badge];

    [NSLayoutConstraint activateConstraints:@[
            [badge.leftAnchor constraintEqualToAnchor:icon.rightAnchor constant:-size / 2],
            [badge.topAnchor constraintEqualToAnchor:icon.topAnchor constant:-size / 2],
            [badge.widthAnchor constraintEqualToConstant:size],
            [badge.heightAnchor constraintEqualToConstant:size]
    ]];
}

- (UITabBarController *)getTabBarController {
    return [[self boundViewController] isKindOfClass:[UITabBarController class]] ? [self boundViewController] : [[self boundViewController] tabBarController];
}
@end
