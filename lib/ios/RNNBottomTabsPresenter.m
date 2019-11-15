#import "RNNBottomTabsPresenter.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+Utils.h"

@implementation RNNBottomTabsPresenter

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
    UITabBarController *bottomTabs = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    [bottomTabs setCurrentTabIndex:[withDefault.bottomTabs.currentTabIndex getWithDefaultValue:0]];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    UITabBarController *bottomTabs = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];

    [bottomTabs setTabBarTestID:[withDefault.bottomTabs.testID getWithDefaultValue:nil]];
    [bottomTabs setTabBarBackgroundColor:[withDefault.bottomTabs.backgroundColor getWithDefaultValue:nil]];
    [bottomTabs setTabBarTranslucent:[withDefault.bottomTabs.translucent getWithDefaultValue:NO]];
    [bottomTabs setTabBarHideShadow:[withDefault.bottomTabs.hideShadow getWithDefaultValue:NO]];
    [bottomTabs setTabBarStyle:[RCTConvert UIBarStyle:[withDefault.bottomTabs.barStyle getWithDefaultValue:@"default"]]];
    [bottomTabs setTabBarVisible:[withDefault.bottomTabs.visible getWithDefaultValue:YES] animated:[withDefault.bottomTabs.animate getWithDefaultValue:NO]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:options resolvedOptions:currentOptions];
    UITabBarController *bottomTabs = self.boundViewController;

    if (options.bottomTabs.currentTabIndex.hasValue) {
        [bottomTabs setCurrentTabIndex:options.bottomTabs.currentTabIndex.get];
        [options.bottomTabs.currentTabIndex consume];
    }

    if (options.bottomTabs.currentTabId.hasValue) {
        [bottomTabs setCurrentTabID:options.bottomTabs.currentTabId.get];
        [options.bottomTabs.currentTabId consume];
    }

    if (options.bottomTabs.testID.hasValue) {
        [bottomTabs setTabBarTestID:options.bottomTabs.testID.get];
    }

    if (options.bottomTabs.backgroundColor.hasValue) {
        [bottomTabs setTabBarBackgroundColor:options.bottomTabs.backgroundColor.get];
    }

    if (options.bottomTabs.barStyle.hasValue) {
        [bottomTabs setTabBarStyle:[RCTConvert UIBarStyle:options.bottomTabs.barStyle.get]];
    }

    if (options.bottomTabs.translucent.hasValue) {
        [bottomTabs setTabBarTranslucent:options.bottomTabs.translucent.get];
    }

    if (options.bottomTabs.hideShadow.hasValue) {
        [bottomTabs setTabBarHideShadow:options.bottomTabs.hideShadow.get];
    }

    if (options.bottomTabs.visible.hasValue) {
        if (options.bottomTabs.animate.hasValue) {
            [bottomTabs setTabBarVisible:options.bottomTabs.visible.get animated:[options.bottomTabs.animate getWithDefaultValue:NO]];
        } else {
            [bottomTabs setTabBarVisible:options.bottomTabs.visible.get animated:NO];
        }
    }
}

- (void)viewDidLayoutSubviews {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self applyDotIndicator];
    });
}

- (void)applyDotIndicator {
    [self.boundViewController forEachChild:^(UIViewController *child) {
        [self applyDotIndicator:child];
    }];
}

@end
