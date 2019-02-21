#import "RNNModalManager.h"
#import "RNNRootViewController.h"
#import "RNNAnimationsTransitionDelegate.h"
#import "RNNLayoutProtocol.h"

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
	[self showModal:viewController animated:animated hasCustomAnimation:NO transitioningDelegate:nil completion:completion];
}

-(void)showModal:(UIViewController<RNNLayoutProtocol> *)viewController animated:(BOOL)animated hasCustomAnimation:(BOOL)hasCustomAnimation transitioningDelegate:(id)transitioningDelegate completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
	if (!viewController) {
		@throw [NSException exceptionWithName:@"ShowUnknownModal" reason:@"showModal called with nil viewController" userInfo:nil];
	}
	
	UIViewController* topVC = [self topPresentedVC];
	topVC.definesPresentationContext = YES;

	if (transitioningDelegate) {
		viewController.transitioningDelegate = transitioningDelegate;
		viewController.modalPresentationStyle = UIModalPresentationCustom;
	} else if (hasCustomAnimation) {
		RNNAnimationsTransitionDelegate* tr = [[RNNAnimationsTransitionDelegate alloc] initWithScreenTransition:viewController.resolveOptions.animations.showModal isDismiss:NO];
		viewController.transitioningDelegate = tr;
	}
	
	[topVC presentViewController:viewController animated:animated completion:^{
		if (completion) {
			completion(nil);
		}
		
		[_presentedModals addObject:viewController.navigationController ? viewController.navigationController : viewController];
	}];
}

- (void)dismissModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion dismissedWithSwipe:(BOOL)dismissedWithSwipe {
	if (viewController) {
		[_pendingModalIdsToDismiss addObject:viewController];
		[self removePendingNextModalIfOnTop:completion dismissedWithSwipe:dismissedWithSwipe];
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


-(void)removePendingNextModalIfOnTop:(RNNTransitionCompletionBlock)completion dismissedWithSwipe:(BOOL)dismissedWithSwipe {
	UIViewController<RNNLayoutProtocol> *modalToDismiss = [_pendingModalIdsToDismiss lastObject];
	RNNNavigationOptions* options = modalToDismiss.resolveOptions;

	if(!modalToDismiss) {
		return;
	}

	UIViewController* topPresentedVC = [self topPresentedVC];
	RNNAnimationsTransitionDelegate* tr = [[RNNAnimationsTransitionDelegate alloc] initWithScreenTransition:modalToDismiss.resolveOptions.animations.dismissModal isDismiss:YES];
	if ([options.animations.dismissModal hasCustomAnimation]) {
		[self topViewControllerParent:modalToDismiss].transitioningDelegate = tr;
	}

	if ((modalToDismiss == topPresentedVC || [[topPresentedVC childViewControllers] containsObject:modalToDismiss]) && !dismissedWithSwipe) {
		[modalToDismiss dismissViewControllerAnimated:[options.animations.dismissModal.enable getWithDefaultValue:YES] completion:^{
			[_pendingModalIdsToDismiss removeObject:modalToDismiss];
			if (modalToDismiss.view) {
				[self dismissedModal:modalToDismiss];
			}
			
			if (completion) {
				completion();
			}
			
			[self removePendingNextModalIfOnTop:nil dismissedWithSwipe:false];
		}];
	} else {
		[modalToDismiss.view removeFromSuperview];
		modalToDismiss.view = nil;
		modalToDismiss.getCurrentChild.resolveOptions.animations.dismissModal.enable = [[Bool alloc] initWithBOOL:NO];
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

- (UIViewController *)topViewControllerParent:(UIViewController *)viewController {
	UIViewController* topParent = viewController;
	while (topParent.parentViewController) {
		topParent = topParent.parentViewController;
	}
	
	return topParent;
}


@end
