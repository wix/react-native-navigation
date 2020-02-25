#import "BottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+RNNOptions.h"
#import "RNNDotIndicatorPresenter.h"
#import "UIViewController+LayoutProtocol.h"

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
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];
    
    if (withDefault.bottomTab.badge.hasValue && [self.boundViewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [self.boundViewController setTabBarItemBadge:withDefault.bottomTab.badge.get];
    }
    
    if (withDefault.bottomTab.badgeColor.hasValue && [self.boundViewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [self.boundViewController setTabBarItemBadgeColor:withDefault.bottomTab.badgeColor.get];
    }
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
    RNNNavigationOptions * withDefault = [options withDefault:self.defaultOptions];
    
    if (withDefault.bottomTab.hasValue) {
        [self updateTabBarItem:self.boundViewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
    }
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    RNNNavigationOptions* withDefault = (RNNNavigationOptions *) [[resolvedOptions withDefault:self.defaultOptions] overrideOptions:options];
    
    if (options.bottomTab.badge.hasValue) {
        [self.boundViewController setTabBarItemBadge:options.bottomTab.badge.get];
    }
    
    if (options.bottomTab.badgeColor.hasValue && [self.boundViewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [self.boundViewController setTabBarItemBadgeColor:options.bottomTab.badgeColor.get];
    }
    
    if ([options.bottomTab.dotIndicator hasValue] && [self.boundViewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [[self dotIndicatorPresenter] apply:self.boundViewController:options.bottomTab.dotIndicator];
    }
    
    if (options.bottomTab.hasValue) {
        [self updateTabBarItem:self.boundViewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
    }
}

- (void)updateTabBarItem:(UITabBarItem *)tabItem bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    self.boundViewController.tabBarItem = [RNNTabBarItemCreator updateTabBarItem:self.boundViewController.tabBarItem bottomTabOptions:bottomTabOptions];
}

- (void)applyDotIndicator:(UIViewController *)child {
    [_dotIndicatorPresenter apply:child:[child resolveOptions].bottomTab.dotIndicator];
}

@end
