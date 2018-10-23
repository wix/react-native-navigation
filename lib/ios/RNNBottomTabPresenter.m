#import "RNNBottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+RNNOptions.h"

@interface RNNBottomTabPresenter()

@property (nonatomic, weak) id bindedViewController;
@property (nonatomic, retain) RNNNavigationOptions* defaultOptions;

@end

@implementation RNNBottomTabPresenter

- (void)bindViewController:(UIViewController *)viewController {
	_bindedViewController = viewController;
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	UIViewController* viewController = self.bindedViewController;
	if ((options.bottomTab.text.hasValue || options.bottomTab.icon.hasValue || options.bottomTab.selectedIcon.hasValue)) {
		RNNNavigationOptions* withDefault = (RNNNavigationOptions *)[[options copy] withDefault:self.defaultOptions];
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:withDefault.bottomTab];
		viewController.tabBarItem = tabItem;
		[options.bottomTab.text consume];
		[options.bottomTab.icon consume];
		[options.bottomTab.selectedIcon consume];
	}
	
	if ([viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
		[viewController rnn_setTabBarItemBadge:[options.bottomTab.badge getWithDefaultValue:nil]];
	}
}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
	UIViewController* viewController = self.bindedViewController;
	if (options.bottomTab.badge.hasValue && [viewController.parentViewController isKindOfClass:[UITabBarController class]]) {
		[viewController rnn_setTabBarItemBadge:options.bottomTab.badge.get];
	}
}

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	_defaultOptions = defaultOptions;
}

@end
