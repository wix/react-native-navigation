#import "RNNViewControllerPresenter.h"
#import "RNNSideMenuController.h"
#import "UIViewController+SideMenuController.h"

@implementation RNNViewControllerPresenter

- (void)bindViewController:(UIViewController *)bindedViewController {
	_bindedViewController = bindedViewController;
}

- (void)presentOnWillMoveToParent:(RNNNavigationOptions *)options {
	[options applyDefaultOptionsOn:self.bindedViewController];
	[options.bottomTab applyOn:self.bindedViewController];
}

- (void)presentOnViewWillAppear:(RNNNavigationOptions *)options {
	UIViewController* viewController = self.bindedViewController;
	UITabBarController* tabBarController = viewController.tabBarController;
	UINavigationController* navigationController = viewController.navigationController;
	RNNSideMenuController* sideMenuController = viewController.sideMenuController;
	
	if (viewController) {
		[self present:options onViewController:viewController];
	}
	
	if (tabBarController) {
		[self present:options onTabBarController:tabBarController];
	}
	
	if (navigationController) {
		[self present:options onNavigationController:navigationController];
	}
	
	if (sideMenuController) {
		[self present:options onSideMenuController:sideMenuController];
	}
}

- (void)present:(RNNNavigationOptions *)options onViewController:(UIViewController *)viewController {
	[options applyOn:viewController];
	[options.topBar applyOn:viewController];
	[options.bottomTabs applyOn:viewController];
	[options.bottomTab applyOn:viewController];
	[options.overlay applyOn:viewController];
	[options.statusBar applyOn:viewController];
	[options.layout applyOn:viewController];
}

- (void)present:(RNNNavigationOptions *)options onNavigationController:(UINavigationController *)navigationController {
	[options applyOnNavigationController:navigationController];
	
	[options.topBar applyOnNavigationController:navigationController];
	[options.statusBar applyOn:navigationController];
	[options.layout applyOn:navigationController];
	[options.bottomTab applyOn:navigationController];
	[options applyOnNavigationController:navigationController];
}

- (void)present:(RNNNavigationOptions *)options onSideMenuController:(RNNSideMenuController *)sideMenuController {
	[options.sideMenu applyOn:self.bindedViewController];
	[options.bottomTab applyOn:self.bindedViewController];
}

- (void)present:(RNNNavigationOptions *)options onTabBarController:(UITabBarController *)tabBarController {
	[options applyOnTabBarController:tabBarController];
	[options.bottomTab applyOn:tabBarController];
}

@end
