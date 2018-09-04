#import "RNNModalManager.h"
#import "RNNRootViewController.h"

@implementation RNNModalManager {
	RNNTransitionWithComponentIdCompletionBlock _completionBlock;
	NSMutableArray* _pendingModalIdsToDismiss;
	NSMutableArray* _presentedModals;
}


-(instancetype)init {
	self = [super init];
	_pendingModalIdsToDismiss = [[NSMutableArray alloc] init];
	_presentedModals = [[NSMutableArray alloc] init];

	return self;
}

-(void)showModal:(BOOL)animated {
	UIViewController* topVC = [self topPresentedVC];
	topVC.definesPresentationContext = YES;
	
	if ([topVC conformsToProtocol:@protocol(RNNRootViewProtocol)]) {
		UIViewController<RNNRootViewProtocol> *navigationTopVC = (UIViewController<RNNRootViewProtocol>*)topVC;
		RNNNavigationOptions* options = navigationTopVC.getLeafViewController.options;
		if (options.animations.showModal.hasCustomAnimation) {
			self.toVC.transitioningDelegate = navigationTopVC;
		}
	}
	
	[topVC presentViewController:self.toVC animated:animated completion:^{
		if (_completionBlock) {
			_completionBlock(self.toVC.getLeafViewController.componentId);
			_completionBlock = nil;
		}
		
		[_presentedModals addObject:self.toVC.getLeafViewController];
		
		self.toVC = nil;
	}];
}

-(void)showModal:(UIViewController *)viewController animated:(BOOL)animated completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
	self.toVC = (UIViewController<RNNRootViewProtocol>*)viewController;
	RNNNavigationOptions* options = self.toVC.getLeafViewController.options;

	_completionBlock = completion;
	
    if ([self.toVC respondsToSelector:@selector(applyModalOptions)]) {
        [self.toVC.getLeafViewController applyModalOptions];
    }
    
    if ([self.toVC respondsToSelector:@selector(isCustomViewController)] &&
        [self.toVC.getLeafViewController isCustomViewController]
    ) {
		[self showModal:animated];
	} else {
		[self.toVC.getLeafViewController waitForReactViewRender:options.animations.showModal.waitForRender perform:^{
			[self showModal:animated];
		}];
	}
}

- (void)dismissModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	if (viewController) {
		[_pendingModalIdsToDismiss addObject:viewController];
		[self removePendingNextModalIfOnTop:completion];
	}
}

-(void)dismissAllModals {
	UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
	[root dismissViewControllerAnimated:YES completion:nil];
	[_delegate dismissedMultipleModals:_presentedModals];
	[_pendingModalIdsToDismiss removeAllObjects];
	[_presentedModals removeAllObjects];
}

#pragma mark - private


-(void)removePendingNextModalIfOnTop:(RNNTransitionCompletionBlock)completion {
	UIViewController<RNNRootViewProtocol> *modalToDismiss = [_pendingModalIdsToDismiss lastObject];
	RNNNavigationOptions* options = modalToDismiss.getLeafViewController.options;

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
		modalToDismiss.getLeafViewController.options.animations.dismissModal.enable = NO;
		[self dismissedModal:modalToDismiss];
		
		if (completion) {
			completion();
		}
	}
}

- (void)dismissedModal:(UIViewController *)viewController {
	[_presentedModals removeObject:viewController];
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
