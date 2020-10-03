#import "BottomTabsBasePresenter.h"
#import "RNNBottomTabsController.h"

@implementation BottomTabsBasePresenter

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
    [super applyOptionsOnInit:options];
    UITabBarController *bottomTabs = self.tabBarController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    [bottomTabs setCurrentTabIndex:[withDefault.bottomTabs.currentTabIndex getWithDefaultValue:0]];
    if ([[withDefault.bottomTabs.titleDisplayMode getWithDefaultValue:@"alwaysShow"] isEqualToString:@"alwaysHide"]) {
        [bottomTabs centerTabItems];
    }
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    RNNBottomTabsController *bottomTabs = self.tabBarController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];

    [bottomTabs setTabBarTestID:[withDefault.bottomTabs.testID getWithDefaultValue:nil]];
    [bottomTabs setTabBarVisible:[withDefault.bottomTabs.visible getWithDefaultValue:YES]];
    
    [bottomTabs.view setBackgroundColor:[withDefault.layout.backgroundColor getWithDefaultValue:nil]];
    [self applyBackgroundColor:[withDefault.bottomTabs.backgroundColor getWithDefaultValue:nil] translucent:[withDefault.bottomTabs.translucent getWithDefaultValue:NO]];
    [bottomTabs setTabBarHideShadow:[withDefault.bottomTabs.hideShadow getWithDefaultValue:NO]];
    [bottomTabs setTabBarStyle:[RCTConvert UIBarStyle:[withDefault.bottomTabs.barStyle getWithDefaultValue:@"default"]]];
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:options resolvedOptions:currentOptions];
    RNNBottomTabsController *bottomTabs = self.tabBarController;

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
        [self setTabBarBackgroundColor:options.bottomTabs.backgroundColor.get];
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
    
    if (options.layout.backgroundColor.hasValue) {
        [bottomTabs.view setBackgroundColor:options.layout.backgroundColor.get];
    }
}

- (RNNBottomTabsController *)tabBarController {
    return (RNNBottomTabsController *)self.boundViewController;
}

- (UITabBar *)tabBar {
    return self.tabBarController.tabBar;
}

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent {
    
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
    
}

- (void)setTabBarTranslucent:(BOOL)translucent {
    
}

@end
