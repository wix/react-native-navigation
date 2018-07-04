
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"

@implementation RNNNavigationController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

- (UIViewController<RNNRootViewProtocol>*)getTopViewController {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController);
}

- (UINavigationController *)navigationController {
	return self;
}

- (BOOL)isCustomViewController {
	return [self.getTopViewController isCustomViewController];
}

- (void)mergeOptions:(RNNOptions *)options {
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

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
	return [super popViewControllerAnimated:animated];
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

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	[self.getTopViewController waitForReactViewRender:wait perform:readyBlock];
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

@end
