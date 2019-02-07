#import "RNNBasePresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNTabBarItemCreator.h"

@implementation RNNBasePresenter

- (void)bindViewController:(UIViewController *)bindedViewController {
	_bindedViewController = bindedViewController;
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {

}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
	UIViewController* viewController = self.bindedViewController;
	if ((options.bottomTab.text.hasValue || options.bottomTab.icon.hasValue || options.bottomTab.selectedIcon.hasValue)) {
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (options.bottomTab.badgeColor.hasValue) {
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (options.bottomTab.textColor.hasValue) {
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (options.bottomTab.iconColor.hasValue) {
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (options.bottomTab.selectedTextColor.hasValue) {
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (options.bottomTab.selectedIconColor.hasValue) {
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		viewController.tabBarItem = tabItem;
	}
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	UIViewController* viewController = self.bindedViewController;

	if (options.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
		[viewController rnn_setTabBarItemBadge:options.bottomTab.badge.get];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	UIViewController* viewController = self.bindedViewController;
	if (newOptions.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
		[viewController rnn_setTabBarItemBadge:newOptions.bottomTab.badge.get];
	}

	if (newOptions.bottomTab.badgeColor.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
		[viewController rnn_setTabBarItemBadgeColor:newOptions.bottomTab.badgeColor.get];
	}

	if ((newOptions.bottomTab.text.hasValue || newOptions.bottomTab.icon.hasValue || newOptions.bottomTab.selectedIcon.hasValue)) {
		RNNNavigationOptions* buttonsResolvedOptions = [(RNNNavigationOptions *)[currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (newOptions.bottomTab.textColor.hasValue) {
		RNNNavigationOptions* buttonsResolvedOptions = [(RNNNavigationOptions *)[currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (newOptions.bottomTab.selectedTextColor.hasValue) {
		RNNNavigationOptions* buttonsResolvedOptions = [(RNNNavigationOptions *)[currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (newOptions.bottomTab.iconColor.hasValue) {
		RNNNavigationOptions* buttonsResolvedOptions = [(RNNNavigationOptions *)[currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
		viewController.tabBarItem = tabItem;
	}

	if (newOptions.bottomTab.selectedIconColor.hasValue) {
		RNNNavigationOptions* buttonsResolvedOptions = [(RNNNavigationOptions *)[currentOptions overrideOptions:newOptions] withDefault:defaultOptions];
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:buttonsResolvedOptions.bottomTab];
		viewController.tabBarItem = tabItem;
	}
}


@end
