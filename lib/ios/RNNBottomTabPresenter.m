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
		UITabBarItem* tabItem = [RNNTabBarItemCreator updateTabBarItem:viewController.tabBarItem bottomTabOptions:options.bottomTab];
		viewController.tabBarItem = tabItem;
		[options.bottomTab.text consume];
		[options.bottomTab.icon consume];
		[options.bottomTab.selectedIcon consume];
	}
}

@end
