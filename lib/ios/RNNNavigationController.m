
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"

@implementation RNNNavigationController

- (instancetype)initWithOptions:(RNNNavigationOptions *)options {
	self = [super init];
	if (self) {
		_options = options;
	}
	
	return self;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

- (UINavigationController *)navigationController {
	return self;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.getLeafViewController.preferredStatusBarStyle;
}

- (UIModalPresentationStyle)modalPresentationStyle {
	return self.getLeafViewController.modalPresentationStyle;
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
	if (self.viewControllers.count > 1) {
		UIViewController *controller = self.viewControllers[self.viewControllers.count - 2];
		if ([controller isKindOfClass:[RNNRootViewController class]]) {
			RNNRootViewController *rnnController = (RNNRootViewController *)controller;
			[rnnController.options applyOn:rnnController];
		}
	}
	
	return [super popViewControllerAnimated:animated];
}

- (NSString *)componentId {
	return _componentId ? _componentId : self.getLeafViewController.componentId;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.options.animations.dismissModal isDismiss:YES];
}

- (UIViewController *)getLeafViewController {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController);
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

- (void)applyTabBarItem {
	[self.options.bottomTab mergeOptions:((RNNNavigationOptions *)self.options.defaultOptions).bottomTab overrideOptions:NO];
	[self.options.bottomTab applyOn:self];
	[self.getLeafViewController.options.bottomTab mergeOptions:((RNNNavigationOptions *)self.getLeafViewController.options.defaultOptions).bottomTab overrideOptions:NO];
	[self.getLeafViewController.options.bottomTab applyOn:self];
}

@end
