#import "RNNNavigationStackManager.h"
#import "RNNErrorHandler.h"
#import <React/RCTI18nUtil.h>

typedef void (^RNNAnimationBlock)(void);

@implementation RNNNavigationStackManager

- (void)push:(UIViewController *)newTop onTop:(UIViewController *)onTopViewController animated:(BOOL)animated animationDelegate:(id)animationDelegate completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	UINavigationController *nvc = onTopViewController.navigationController;

	if([[RCTI18nUtil sharedInstance] isRTL]) {
		nvc.view.semanticContentAttribute = UISemanticContentAttributeForceRightToLeft;
		nvc.navigationBar.semanticContentAttribute = UISemanticContentAttributeForceRightToLeft;
	} else {
		nvc.view.semanticContentAttribute = UISemanticContentAttributeForceLeftToRight;
		nvc.navigationBar.semanticContentAttribute = UISemanticContentAttributeForceLeftToRight;
	}

	if (animationDelegate) {
		nvc.delegate = animationDelegate;
	} else {
		nvc.delegate = nil;
	}
	
	[self performAnimationBlock:^{
		[nvc pushViewController:newTop animated:animated];
	} completion:completion];
}

- (void)pop:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection {
	if (!viewController.view.window) {
		animated = NO;
	}
	
	__block UIViewController *poppedVC = nil;
	[self performAnimationBlock:^{
		poppedVC = [viewController.navigationController popViewControllerAnimated:animated];
	} completion:^{
		if (poppedVC) {
			completion();
		} else {
			[RNNErrorHandler reject:rejection withErrorCode:1012 errorDescription:@"popping component failed"];
		}
	}];
}

- (void)popTo:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNPopCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection; {
	__block NSArray* poppedVCs;
	
	if ([viewController.navigationController.childViewControllers containsObject:viewController]) {
		[self performAnimationBlock:^{
			poppedVCs = [viewController.navigationController popToViewController:viewController animated:animated];
		} completion:^{
			if (completion) {
				completion(poppedVCs);
			}
		}];
	} else {
		[RNNErrorHandler reject:rejection withErrorCode:1011 errorDescription:@"component not found in stack"];
	}
}

- (void)popToRoot:(UIViewController*)viewController animated:(BOOL)animated completion:(RNNPopCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection {
	__block NSArray* poppedVCs;
	
	[self performAnimationBlock:^{
		poppedVCs = [viewController.navigationController popToRootViewControllerAnimated:animated];
	} completion:^{
		completion(poppedVCs);
	}];
}

- (void)setStackChildren:(NSArray<UIViewController *> *)children fromViewController:(UIViewController *)fromViewController animated:(BOOL)animated completion:(RNNTransitionCompletionBlock)completion rejection:(RNNTransitionRejectionBlock)rejection {
	UINavigationController* nvc = fromViewController.navigationController;
	
	[self performAnimationBlock:^{
		[nvc setViewControllers:children animated:animated];
	} completion:completion];
}

# pragma mark Private

- (void)performAnimationBlock:(RNNAnimationBlock)animationBlock completion:(RNNTransitionCompletionBlock)completion {
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		if (completion) {
			completion();
		}
	}];
	
	animationBlock();
	
	[CATransaction commit];
}


@end
