
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"

@implementation RNNNavigationController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

- (UIViewController<RNNRootViewProtocol>*)getTopViewController {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController);
}

- (BOOL)isCustomTransitioned {
	return NO;
}

- (BOOL)isCustomViewController {
	return [self.getTopViewController isCustomViewController];
}

- (void)mergeOptions:(NSDictionary *)options {
	[self.getTopViewController mergeOptions:options];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.getTopViewController.preferredStatusBarStyle;
}

- (UIModalPresentationStyle)modalPresentationStyle {
	return self.getTopViewController.modalPresentationStyle;
}

- (void)applyModalOptions {
	[self.getTopViewController applyModalOptions];
}

- (NSString *)componentId {
	return _componentId ? _componentId : self.getTopViewController.componentId;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.dismissModal isDismiss:YES];
}

- (RNNNavigationOptions *)options {
	return self.getTopViewController.options;
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

@end
