#import "RNNModalManager.h"
#import "RNNRootViewController.h"

@implementation RNNModalManager {
	NSMutableArray* _pendingModalIdsToDismiss;
	NSMutableArray* _presentedModals;
}


-(instancetype)init {
	self = [super init];
	_pendingModalIdsToDismiss = [[NSMutableArray alloc] init];
	_presentedModals = [[NSMutableArray alloc] init];

	return self;
}

-(void)showModal:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
	[self showModal:viewController animated:animated hasCustomAnimation:NO completion:completion];
}

-(void)showModal:(UIViewController *)viewController animated:(BOOL)animated hasCustomAnimation:(BOOL)hasCustomAnimation completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
	if (!viewController) {
		@throw [NSException exceptionWithName:@"ShowUnknownModal" reason:@"showModal called with nil viewController" userInfo:nil];
	}
	
	UIViewController* topVC = [self topPresentedVC];
	topVC.definesPresentationContext = YES;
	
	if (hasCustomAnimation) {
		viewController.transitioningDelegate = (UIViewController<UIViewControllerTransitioningDelegate>*)topVC;
	}
	
	[topVC presentViewController:viewController animated:animated completion:^{
		if (completion) {
			completion(nil);
		}
		
		[_presentedModals addObject:viewController.navigationController ? viewController.navigationController : viewController];
	}];
}

- (void)dismissModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	if (viewController) {
		[_pendingModalIdsToDismiss addObject:viewController];
		[self removePendingNextModalIfOnTop:completion];
	}
}

-(void)dismissAllModalsAnimated:(BOOL)animated {
	UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
	[root dismissViewControllerAnimated:animated completion:nil];
	[_delegate dismissedMultipleModals:_presentedModals];
	[_pendingModalIdsToDismiss removeAllObjects];
	[_presentedModals removeAllObjects];
}

#pragma mark - private


-(void)removePendingNextModalIfOnTop:(RNNTransitionCompletionBlock)completion {
	UIViewController<RNNParentProtocol> *modalToDismiss = [_pendingModalIdsToDismiss lastObject];
	RNNNavigationOptions* options = modalToDismiss.getCurrentChild.resolveOptions;

	if(!modalToDismiss) {
		return;
	}

	UIViewController* topPresentedVC = [self topPresentedVC];

	if ([options.animations.showModal hasCustomAnimation]) {
		modalToDismiss.transitioningDelegate = modalToDismiss;
	}

	if (modalToDismiss == topPresentedVC || [[topPresentedVC childViewControllers] containsObject:modalToDismiss]) {
		[modalToDismiss dismissViewControllerAnimated:options.animations.dismissModal.enable completion:^{
			[_pendingModalIdsToDismiss removeObject:modalToDismiss];
			if (modalToDismiss.view) {
				[self dismissedModal:modalToDismiss];
			}
			
			if (completion) {
				completion();
			}
			
			[self removePendingNextModalIfOnTop:nil];
		}];
	} else {
		[modalToDismiss.view removeFromSuperview];
		modalToDismiss.view = nil;
		modalToDismiss.getCurrentChild.resolveOptions.animations.dismissModal.enable = NO;
		[self dismissedModal:modalToDismiss];
		
		if (completion) {
			completion();
		}
	}
}

- (void)dismissedModal:(UIViewController *)viewController {
	[_presentedModals removeObject:viewController.navigationController ? viewController.navigationController : viewController];
	[_delegate dismissedModal:viewController];
}

-(UIViewController*)topPresentedVC {
	UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
	while(root.presentedViewController) {
		root = root.presentedViewController;
	}
	return root;
}

-(UIViewController*)topPresentedVCLeaf {
	id root = [self topPresentedVC];
	return [root topViewController] ? [root topViewController] : root;
}


@end
