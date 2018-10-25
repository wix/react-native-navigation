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
		[options.bottomTab.text consume];
		[options.bottomTab.icon consume];
		[options.bottomTab.selectedIcon consume];
	}
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	UIViewController* viewController = self.bindedViewController;
	
	if ([viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
		[viewController rnn_setTabBarItemBadge:[options.bottomTab.badge getWithDefaultValue:nil]];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)newOptions currentOptions:(RNNNavigationOptions *)currentOptions defaultOptions:(RNNNavigationOptions *)defaultOptions {
	UIViewController* viewController = self.bindedViewController;
	if (newOptions.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
		[viewController rnn_setTabBarItemBadge:newOptions.bottomTab.badge.get];
	}
}


@end
