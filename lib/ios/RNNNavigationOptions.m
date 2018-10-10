#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"
#import "RNNSplitViewController.h"
#import "RNNNavigationButtons.h"
#import "UIViewController+RNNOptions.h"
#import "UINavigationController+RNNOptions.h"

@implementation RNNNavigationOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.backgroundImage) {
		[viewController rnn_setBackgroundImage:[RCTConvert UIImage:self.backgroundImage]];
	}
	
	if (self.modalPresentationStyle) {
		[viewController rnn_setModalPresentationStyle:self.modalPresentationStyle];
	}
	
	if (self.modalTransitionStyle) {
		[viewController rnn_setModalTransitionStyle:self.modalTransitionStyle];
	}
}

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	if (self.popGesture) {
		[navigationController rnn_setInteractivePopGestureEnabled:[self.popGesture boolValue]];
	}
	
	if (self.rootBackgroundImage) {
		[navigationController rnn_setRootBackgroundImage:[RCTConvert UIImage:self.rootBackgroundImage]];
	}
}

- (void)applyOnTabBarController:(UITabBarController *)tabBarController {
	[self.bottomTabs applyOnTabBarController:tabBarController];
}

- (void)applyDefaultOptionsOn:(UIViewController *)viewController {
	UITabBarController* tabBarController = viewController.tabBarController;
	UINavigationController* navigationController = viewController.navigationController;
	
	[navigationController setNavigationBarHidden:NO animated:NO];
	navigationController.hidesBarsOnSwipe = NO;
	navigationController.navigationBar.barStyle = UIBarStyleDefault;
	navigationController.navigationBar.translucent = NO;
	navigationController.navigationBar.clipsToBounds = NO;
	
	tabBarController.tabBar.barTintColor = nil;
	tabBarController.tabBar.barStyle = UIBarStyleDefault;
	tabBarController.tabBar.translucent = NO;
}

- (void)resetOptions {
	[self.sideMenu resetOptions];
	[self.bottomTabs resetOptions];
	[self.bottomTab resetOptions];
}

@end
