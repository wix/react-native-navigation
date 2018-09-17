
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"
#import "RNNRootViewController.h"

@implementation RNNNavigationController

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
			[rnnController.presenter presentOn:rnnController];
		}
	}
	
	return [super popViewControllerAnimated:animated];
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.presenter.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.presenter.options.animations.dismissModal isDismiss:YES];
}

- (UIViewController *)getLeafViewController {
	return ((UIViewController<RNNParentProtocol>*)self.topViewController);
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

- (void)willMoveToParentViewController:(UIViewController *)parent {
	if ([self.parentViewController respondsToSelector:@selector(performOnChildLoad:)]) {
		[self.parentViewController performSelector:@selector(performOnChildLoad:) withObject:_presenter.options];
	}
}

- (void)performOnChildWillAppear:(RNNNavigationOptions *)options {
	[_presenter overrideOptions:options];
	[_presenter presentOn:self];
	if ([self.parentViewController respondsToSelector:@selector(performOnChildWillAppear:)]) {
		[self.parentViewController performSelector:@selector(performOnChildWillAppear:) withObject:_presenter.options];
	}
}

- (void)performOnChildLoad:(RNNNavigationOptions *)options {
	[_presenter overrideOptions:options];
	[_presenter presentOn:self];
	if ([self.parentViewController respondsToSelector:@selector(performOnChildLoad:)]) {
		[self.parentViewController performSelector:@selector(performOnChildLoad:) withObject:_presenter.options];
	}
}

@end
