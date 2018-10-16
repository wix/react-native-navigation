#import "RNNBottomTabPresenter.h"
#import "RNNTabBarItemCreator.h"

@implementation RNNBottomTabPresenter

- (instancetype)initWithViewController:(UIViewController *)viewController {
	self = [super init];
	
	self.bindedViewController = viewController;
	
	return self;
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
}

@end
