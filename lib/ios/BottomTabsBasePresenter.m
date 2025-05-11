#import "BottomTabsBasePresenter.h"
#import "RNNBottomTabsController.h"
#import "UIImage+utils.h"

@implementation BottomTabsBasePresenter

- (void)applyOptionsOnInit:(RNNNavigationOptions *)options {
    [super applyOptionsOnInit:options];
    UITabBarController *bottomTabs = self.tabBarController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    [bottomTabs setCurrentTabIndex:[withDefault.bottomTabs.currentTabIndex withDefault:0]];
    if (withDefault.bottomTabs.currentTabId.hasValue) {
        [bottomTabs setCurrentTabID:withDefault.bottomTabs.currentTabId.get];
    }
    if ([[withDefault.bottomTabs.titleDisplayMode withDefault:@"alwaysShow"]
            isEqualToString:@"alwaysHide"]) {
        [bottomTabs centerTabItems];
    }
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    RNNBottomTabsController *bottomTabs = self.tabBarController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];

    [bottomTabs setTabBarTestID:[withDefault.bottomTabs.testID withDefault:nil]];
    [bottomTabs setTabBarVisible:[withDefault.bottomTabs.visible withDefault:YES]];

    [bottomTabs.view setBackgroundColor:[withDefault.layout.backgroundColor withDefault:nil]];
    [self applyBackgroundColor:[withDefault.bottomTabs.backgroundColor withDefault:nil]
                   translucent:[withDefault.bottomTabs.translucent withDefault:NO]];
    [bottomTabs setTabBarHideShadow:[withDefault.bottomTabs.hideShadow withDefault:NO]];
    [bottomTabs setTabBarStyle:[RCTConvert UIBarStyle:[withDefault.bottomTabs.barStyle
                                                          withDefault:@"default"]]];
    [self applyTabBarBorder:withDefault.bottomTabs];
    [self applyTabBarShadow:withDefault.bottomTabs.shadow];
}

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)currentOptions {
    [super mergeOptions:mergeOptions resolvedOptions:currentOptions];
    RNNBottomTabsController *bottomTabs = self.tabBarController;
    RNNNavigationOptions *withDefault = [mergeOptions withDefault:[self defaultOptions]];

    if (mergeOptions.bottomTabs.currentTabIndex.hasValue) {
        [bottomTabs setCurrentTabIndex:mergeOptions.bottomTabs.currentTabIndex.get];
        [mergeOptions.bottomTabs.currentTabIndex consume];
    }

    if (mergeOptions.bottomTabs.currentTabId.hasValue) {
        [bottomTabs setCurrentTabID:mergeOptions.bottomTabs.currentTabId.get];
        [mergeOptions.bottomTabs.currentTabId consume];
    }

    if (mergeOptions.bottomTabs.testID.hasValue) {
        [bottomTabs setTabBarTestID:mergeOptions.bottomTabs.testID.get];
    }

    if (mergeOptions.bottomTabs.backgroundColor.hasValue) {
        [self setTabBarBackgroundColor:mergeOptions.bottomTabs.backgroundColor.get];
    }

    if (mergeOptions.bottomTabs.barStyle.hasValue) {
        [bottomTabs setTabBarStyle:[RCTConvert UIBarStyle:mergeOptions.bottomTabs.barStyle.get]];
    }

    if (mergeOptions.bottomTabs.translucent.hasValue) {
        [bottomTabs setTabBarTranslucent:mergeOptions.bottomTabs.translucent.get];
    }

    if (mergeOptions.bottomTabs.hideShadow.hasValue) {
        [bottomTabs setTabBarHideShadow:mergeOptions.bottomTabs.hideShadow.get];
    }

    if (mergeOptions.bottomTabs.visible.hasValue) {
        if (mergeOptions.bottomTabs.animate.hasValue) {
            [bottomTabs setTabBarVisible:mergeOptions.bottomTabs.visible.get
                                animated:[mergeOptions.bottomTabs.animate withDefault:NO]];
        } else {
            [bottomTabs setTabBarVisible:mergeOptions.bottomTabs.visible.get animated:NO];
        }
    }

    if (mergeOptions.layout.backgroundColor.hasValue) {
        [bottomTabs.view setBackgroundColor:mergeOptions.layout.backgroundColor.get];
    }

    if (mergeOptions.bottomTabs.borderColor.hasValue ||
        mergeOptions.bottomTabs.borderWidth.hasValue) {
        [self applyTabBarBorder:withDefault.bottomTabs];
    }

    if (mergeOptions.bottomTabs.shadow.hasValue) {
        [self applyTabBarShadow:withDefault.bottomTabs.shadow];
    }
}

- (RNNBottomTabsController *)tabBarController {
    return (RNNBottomTabsController *)self.boundViewController;
}

- (UITabBar *)tabBar {
    return self.tabBarController.tabBar;
}

- (void)applyTabBarBorder:(RNNBottomTabsOptions *)options {
    if (options.borderColor.hasValue || options.borderWidth.hasValue) {
        self.tabBar.backgroundImage = UIImage.new;
        self.tabBar.shadowImage = [UIImage
            imageWithSize:CGSizeMake(1, [[options.borderWidth withDefault:@(1)] floatValue])
                    color:[options.borderColor withDefault:UIColor.blackColor]];
    }
}

- (void)applyTabBarShadow:(RNNShadowOptions *)options {
    self.tabBar.layer.shadowRadius =
        [options.radius withDefault:@(self.tabBar.layer.shadowRadius)].floatValue;
    self.tabBar.layer.shadowColor =
        [options.color withDefault:[UIColor colorWithCGColor:self.tabBar.layer.shadowColor]]
            .CGColor;
    self.tabBar.layer.shadowOpacity =
        [options.opacity withDefault:@(self.tabBar.layer.shadowOpacity)].floatValue;
}

- (void)applyBackgroundColor:(UIColor *)backgroundColor translucent:(BOOL)translucent {
}

- (void)setTabBarBackgroundColor:(UIColor *)backgroundColor {
}

- (void)setTabBarTranslucent:(BOOL)translucent {
}

@end
