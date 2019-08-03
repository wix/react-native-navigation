#import "RNNBasePresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNTabBarItemCreator.h"
#import "RNNReactComponentRegistry.h"
#import "UIViewController+LayoutProtocol.h"
#import "DotIndicatorOptions.h"
#import "RNNDotIndicatorPresenter.h"

@interface RNNBasePresenter ()
@property(nonatomic, strong) RNNDotIndicatorPresenter* dotIndicatorPresenter;
@end

@implementation RNNBasePresenter

-(instancetype)init {
    self = [super init];
    self.dotIndicatorPresenter = [RNNDotIndicatorPresenter new];
    return self;
}

- (void)bindViewController:(UIViewController <RNNLayoutProtocol> *)boundViewController {
    self.boundComponentId = boundViewController.layoutInfo.componentId;
    _boundViewController = boundViewController;
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

    if ([newOptions.bottomTab.dotIndicator hasValue] && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [[self dotIndicatorPresenter] apply:viewController:newOptions.bottomTab.dotIndicator];
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

- (void)applyDotIndicator:(UIViewController *)child {
    [[self dotIndicatorPresenter] apply:child:[child resolveOptions].bottomTab.dotIndicator];
}
@end
