
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"

@implementation RNNNavigationController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

- (BOOL)isCustomTransitioned {
	return NO;
}

- (BOOL)isCustomViewController {
	return [((UIViewController<RNNRootViewProtocol>*)self.topViewController) isCustomViewController];
}

- (void)mergeOptions:(NSDictionary *)options {
	[((UIViewController<RNNRootViewProtocol>*)self.topViewController) mergeOptions:options];
}

- (NSString *)componentId {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController).componentId;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.options.animations.dismissModal isDismiss:YES];
}

- (RNNNavigationOptions *)options {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController).options;
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

@end
