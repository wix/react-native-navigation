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

    if (withDefault.bottomTab.hasValue) {
        [self updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
    }
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions* withDefault = (RNNNavigationOptions *) [[resolvedOptions withDefault:self.defaultOptions] overrideOptions:options];
    
    if ([options.bottomTab.dotIndicator hasValue] && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
        [[self dotIndicatorPresenter] apply:viewController:options.bottomTab.dotIndicator];
    }
    
    if (options.bottomTab.hasValue) {
        [self updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
    }
}

- (void)updateTabBarItem:(UITabBarItem *)tabItem bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions {
    UIViewController *viewController = self.boundViewController;
    viewController.tabBarItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:bottomTabOptions];
}

- (void)applyDotIndicator:(UIViewController *)child {
    [_dotIndicatorPresenter apply:child:[child resolveOptions].bottomTab.dotIndicator];
}

@end
