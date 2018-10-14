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
	
	[self applyOtherOptionsOn:viewController];
	
	[self applyOnNavigationController:viewController.navigationController];
	[self applyOnTabBarController:viewController.tabBarController];
}

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	[self.topBar applyOnNavigationController:navigationController];
	[self.statusBar applyOn:navigationController];
	[self.layout applyOn:navigationController];
	[self.bottomTab applyOn:navigationController];
	[self applyOtherOptionsOnNavigationController:navigationController];
}

- (void)applyOnTabBarController:(UITabBarController *)tabBarController {
	[self.bottomTabs applyOnTabBarController:tabBarController];
}

- (void)applyOtherOptionsOnNavigationController:(UINavigationController*)navigationController {
	if (self.popGesture) {
		navigationController.interactivePopGestureRecognizer.enabled = [self.popGesture boolValue];
	}
	
	if (self.rootBackgroundImage) {
		UIImageView* backgroundImageView = (navigationController.view.subviews.count > 0) ? navigationController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:navigationController.view.bounds];
			[navigationController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = [self.rootBackgroundImage isKindOfClass:[UIImage class]] ? (UIImage*)self.rootBackgroundImage : [RCTConvert UIImage:self.rootBackgroundImage];
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
}

- (void)applyOtherOptionsOn:(UIViewController*)viewController {
	if (self.backgroundImage) {
		UIImageView* backgroundImageView = (viewController.view.subviews.count > 0) ? viewController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:viewController.view.bounds];
			[viewController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = [self.backgroundImage isKindOfClass:[UIImage class]] ? (UIImage*)self.backgroundImage : [RCTConvert UIImage:self.backgroundImage];
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}

	if (self.modalPresentationStyle) {
		viewController.modalPresentationStyle = [RCTConvert UIModalPresentationStyle:self.modalPresentationStyle];
		[viewController.view setBackgroundColor:[UIColor clearColor]];
	}
	if (self.modalTransitionStyle) {
		viewController.modalTransitionStyle = [RCTConvert UIModalTransitionStyle:self.modalTransitionStyle];
	}
}

@end
