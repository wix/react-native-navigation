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

-(instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    _defaultOptions = defaultOptions;
    self.dotIndicatorPresenter = [[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:_defaultOptions];
    return self;
}

- (void)bindViewController:(UIViewController <RNNLayoutProtocol> *)boundViewController {
    self.boundComponentId = boundViewController.layoutInfo.componentId;
    _boundViewController = boundViewController;
}

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    _defaultOptions = defaultOptions;
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {

}

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options {

}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
    UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions * withDefault = [options withDefault:_defaultOptions];

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

- (void)applyOptions:(RNNNavigationOptions *)options {
    UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions * withDefault = [options withDefault:_defaultOptions];

    if (withDefault.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController setTabBarItemBadge:withDefault.bottomTab.badge.get];
    }

    if (withDefault.bottomTab.badgeColor.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController setTabBarItemBadgeColor:withDefault.bottomTab.badgeColor.get];
    }
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    UIViewController *viewController = self.boundViewController;
    if (options.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController setTabBarItemBadge:options.bottomTab.badge.get];
    }

    if (options.bottomTab.badgeColor.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [viewController setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
    }

    if ([options.bottomTab.dotIndicator hasValue] && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [[self dotIndicatorPresenter] apply:viewController:options.bottomTab.dotIndicator];
    }

    if (options.bottomTab.text.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = (RNNNavigationOptions *) [resolvedOptions overrideOptions:options];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.icon.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = (RNNNavigationOptions *) [resolvedOptions overrideOptions:options];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedIcon.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = (RNNNavigationOptions *) [resolvedOptions overrideOptions:options];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.textColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = (RNNNavigationOptions *) [resolvedOptions overrideOptions:options];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedTextColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = (RNNNavigationOptions *) [resolvedOptions overrideOptions:options];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.iconColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = (RNNNavigationOptions *) [resolvedOptions overrideOptions:options];
        UITabBarItem *tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
        viewController.tabBarItem = tabItem;
    }

    if (options.bottomTab.selectedIconColor.hasValue) {
        RNNNavigationOptions *buttonsResolvedOptions = (RNNNavigationOptions *) [resolvedOptions overrideOptions:options];
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

- (UIStatusBarStyle)getStatusBarStyle:(RNNNavigationOptions *)resolvedOptions {
    RNNNavigationOptions *withDefault = [resolvedOptions withDefault:[self defaultOptions]];
    if ([[withDefault.statusBar.style getWithDefaultValue:@"default"] isEqualToString:@"light"]) {
        return UIStatusBarStyleLightContent;
    } else {
        return UIStatusBarStyleDefault;
    }
}

- (UIInterfaceOrientationMask)getOrientation:(RNNNavigationOptions *)options {
    return [options withDefault:[self defaultOptions]].layout.supportedOrientations;
}

- (BOOL)isStatusBarVisibility:(UINavigationController *)stack resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    RNNNavigationOptions *withDefault = [resolvedOptions withDefault:[self defaultOptions]];
    if (withDefault.statusBar.visible.hasValue) {
        return ![withDefault.statusBar.visible get];
    } else if ([withDefault.statusBar.hideWithTopBar getWithDefaultValue:NO]) {
        return stack.isNavigationBarHidden;
    }
    return NO;
}


@end
