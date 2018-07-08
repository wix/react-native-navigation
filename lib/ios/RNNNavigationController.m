
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"

@implementation RNNNavigationController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

- (UINavigationController *)navigationController {
	return self;
}

- (BOOL)isCustomViewController {
	return [self.getLeafViewController isCustomViewController];
}

- (void)mergeOptions:(RNNOptions *)options {
	[self.getLeafViewController mergeOptions:options];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.getLeafViewController.preferredStatusBarStyle;
}

- (UIModalPresentationStyle)modalPresentationStyle {
	return self.getLeafViewController.modalPresentationStyle;
}

- (void)applyModalOptions {
	[self.getLeafViewController applyModalOptions];
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
	return [super popViewControllerAnimated:animated];
}

- (NSString *)componentId {
	return _componentId ? _componentId : self.getLeafViewController.componentId;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.dismissModal isDismiss:YES];
}

- (RNNNavigationOptions *)options {
	return self.getLeafViewController.options;
}

- (UIViewController *)getLeafViewController {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController);
}

- (void)waitForReactViewRender:(BOOL)wait perform:(RNNReactViewReadyCompletionBlock)readyBlock {
	[self.getLeafViewController waitForReactViewRender:wait perform:readyBlock];
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

@end
