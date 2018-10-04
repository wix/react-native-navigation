#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"
#import "RNNSplitViewController.h"
#import "RNNNavigationButtons.h"

@implementation RNNNavigationOptions

- (void)applyOn:(UIViewController *)viewController {
	[self.topBar applyOn:viewController];
	[self.bottomTabs applyOn:viewController];
	[self.topTab applyOn:viewController];
	[self.bottomTab applyOn:viewController];
	[self.sideMenu applyOn:viewController];
	[self.overlay applyOn:viewController];
	[self.statusBar applyOn:viewController];
	[self.layout applyOn:viewController];
	[self applyOtherOptions:self on:viewController];
}

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	[self.topBar applyOnNavigationController:navigationController];
	[self.statusBar applyOn:navigationController];
	[self.layout applyOn:navigationController];
	[self.bottomTab applyOn:navigationController];
	[self applyOtherOptions:self onNavigationController:navigationController];
}

- (void)applyOnTabBarController:(UITabBarController *)tabBarController {
	[self.bottomTabs applyOnTabBarController:tabBarController];
}

- (void)applyOtherOptions:(RNNNavigationOptions *)options onNavigationController:(UINavigationController*)navigationController {
	if (options.popGesture) {
		navigationController.interactivePopGestureRecognizer.enabled = [options.popGesture boolValue];
	}
	
	if (options.rootBackgroundImage) {
		UIImageView* backgroundImageView = (navigationController.view.subviews.count > 0) ? navigationController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:navigationController.view.bounds];
			[navigationController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = [options.rootBackgroundImage isKindOfClass:[UIImage class]] ? (UIImage*)options.rootBackgroundImage : [RCTConvert UIImage:options.rootBackgroundImage];
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
}

- (void)applyOtherOptions:(RNNNavigationOptions *)options on:(UIViewController*)viewController {
	if (options.backgroundImage) {
		UIImageView* backgroundImageView = (viewController.view.subviews.count > 0) ? viewController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:viewController.view.bounds];
			[viewController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = [options.backgroundImage isKindOfClass:[UIImage class]] ? (UIImage*)options.backgroundImage : [RCTConvert UIImage:options.backgroundImage];
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}

	if (options.modalPresentationStyle) {
		viewController.modalPresentationStyle = [RCTConvert UIModalPresentationStyle:options.modalPresentationStyle];
		[viewController.view setBackgroundColor:[UIColor clearColor]];
	}
	if (options.modalTransitionStyle) {
		viewController.modalTransitionStyle = [RCTConvert UIModalTransitionStyle:options.modalTransitionStyle];
	}
}

@end
