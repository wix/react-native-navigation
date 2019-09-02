#import "RNNTabBarPresenter.h"
#import "UITabBarController+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+Utils.h"

@implementation RNNTabBarPresenter

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
    UITabBarController *tabBarController = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    [tabBarController rnn_setCurrentTabIndex:[withDefault.bottomTabs.currentTabIndex getWithDefaultValue:0]];
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    UITabBarController *tabBarController = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];

    [tabBarController rnn_setTabBarTestID:[withDefault.bottomTabs.testID getWithDefaultValue:nil]];
    [tabBarController rnn_setTabBarBackgroundColor:[withDefault.bottomTabs.backgroundColor getWithDefaultValue:nil]];
    [tabBarController rnn_setTabBarTranslucent:[withDefault.bottomTabs.translucent getWithDefaultValue:NO]];
    [tabBarController rnn_setTabBarHideShadow:[withDefault.bottomTabs.hideShadow getWithDefaultValue:NO]];
    [tabBarController rnn_setTabBarStyle:[RCTConvert UIBarStyle:[withDefault.bottomTabs.barStyle getWithDefaultValue:@"default"]]];
    [tabBarController rnn_setTabBarVisible:[withDefault.bottomTabs.visible getWithDefaultValue:YES] animated:[withDefault.bottomTabs.animate getWithDefaultValue:NO]];
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:newOptions currentOptions:currentOptions];

    UITabBarController *tabBarController = self.boundViewController;

    if (newOptions.bottomTabs.currentTabIndex.hasValue) {
        [tabBarController rnn_setCurrentTabIndex:newOptions.bottomTabs.currentTabIndex.get];
        [newOptions.bottomTabs.currentTabIndex consume];
    }

    if (newOptions.bottomTabs.currentTabId.hasValue) {
        [tabBarController rnn_setCurrentTabID:newOptions.bottomTabs.currentTabId.get];
        [newOptions.bottomTabs.currentTabId consume];
    }

    if (newOptions.bottomTabs.testID.hasValue) {
        [tabBarController rnn_setTabBarTestID:newOptions.bottomTabs.testID.get];
    }

    if (newOptions.bottomTabs.backgroundColor.hasValue) {
        [tabBarController rnn_setTabBarBackgroundColor:newOptions.bottomTabs.backgroundColor.get];
    }

    if (newOptions.bottomTabs.barStyle.hasValue) {
        [tabBarController rnn_setTabBarStyle:[RCTConvert UIBarStyle:newOptions.bottomTabs.barStyle.get]];
    }

    if (newOptions.bottomTabs.translucent.hasValue) {
        [tabBarController rnn_setTabBarTranslucent:newOptions.bottomTabs.translucent.get];
    }

    if (newOptions.bottomTabs.hideShadow.hasValue) {
        [tabBarController rnn_setTabBarHideShadow:newOptions.bottomTabs.hideShadow.get];
    }

    if (newOptions.bottomTabs.visible.hasValue) {
        if (newOptions.bottomTabs.animate.hasValue) {
            [tabBarController rnn_setTabBarVisible:newOptions.bottomTabs.visible.get animated:[newOptions.bottomTabs.animate getWithDefaultValue:NO]];
        } else {
            [tabBarController rnn_setTabBarVisible:newOptions.bottomTabs.visible.get animated:NO];
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
