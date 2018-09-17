
#import "RNNNavigationController.h"
#import "RNNModalAnimation.h"

@implementation RNNNavigationController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo {
	self = [super init];
	if (self) {
		_layoutInfo = layoutInfo;
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
			[rnnController.layoutInfo.options applyOn:rnnController];
		}
	}
	
	return [super popViewControllerAnimated:animated];
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.layoutInfo.options.animations.showModal isDismiss:NO];
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return [[RNNModalAnimation alloc] initWithScreenTransition:self.getLeafViewController.layoutInfo.options.animations.dismissModal isDismiss:YES];
}

- (UIViewController *)getLeafViewController {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController);
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

- (void)setViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers {
	[super setViewControllers:viewControllers];
    for (UIViewController<RNNRootViewProtocol>* viewController in viewControllers) {
        if ([viewController respondsToSelector:@selector(layoutInfo)]) {
            [viewController.layoutInfo.options mergeOptions:self.layoutInfo.options overrideOptions:NO];
        }
    }
}

- (void)pushViewController:(UIViewController<RNNRootViewProtocol> *)viewController animated:(BOOL)animated {
	[super pushViewController:viewController animated:animated];
    if ([viewController respondsToSelector:@selector(layoutInfo)]) {
		[viewController.layoutInfo.options mergeOptions:self.layoutInfo.options overrideOptions:NO];
	}
}

@end
