#import "RNNModalManager.h"
#import "RNNRootViewController.h"

@implementation RNNModalManager {
	RNNStore *_store;
	RNNTransitionCompletionBlock _completionBlock;
}


-(instancetype)initWithStore:(RNNStore*)store {
	self = [super init];
	_store = store;
	return self;
}

-(void)showModalAfterLoad:(NSDictionary*)notif {
	RNNRootViewController *topVC = (RNNRootViewController*)[self topPresentedVC];
	topVC.definesPresentationContext = YES;
	
	if (topVC.options.animations.showModal.hasCustomAnimation) {
		self.toVC.transitioningDelegate = topVC;
	}
	
	[topVC presentViewController:self.toVC animated:self.toVC.options.animations.showModal.enable completion:^{
		if (_completionBlock) {
			_completionBlock();
			_completionBlock = nil;
		}
		self.toVC = nil;
	}];
}

-(void)showModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	self.toVC = (UIViewController<RNNRootViewProtocol>*)viewController;
	_completionBlock = completion;
	
    if ([self.toVC respondsToSelector:@selector(applyModalOptions)]) {
        [self.toVC applyModalOptions];
    }
    
    if ([self.toVC respondsToSelector:@selector(isCustomViewController)] &&
        [self.toVC isCustomViewController]
    ) {
		[self showModalAfterLoad:nil];
	} else {
		[self.toVC waitForReactViewRender:self.toVC.options.animations.showModal.waitForRender perform:^{
			[self showModalAfterLoad:nil];
		}];
	}
}

-(void)dismissModal:(NSString *)componentId {
	[[_store pendingModalIdsToDismiss] addObject:componentId];
	[self removePendingNextModalIfOnTop];
}

-(void)dismissAllModals {
	UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
	[root dismissViewControllerAnimated:YES completion:nil];
	[[_store pendingModalIdsToDismiss] removeAllObjects];
}

#pragma mark - private


-(void)removePendingNextModalIfOnTop {
	NSString *componentId = [[_store pendingModalIdsToDismiss] lastObject];

	UIViewController<RNNRootViewProtocol> *modalToDismiss = (UIViewController<RNNRootViewProtocol>*)[_store findComponentForId:componentId];

	if(!modalToDismiss) {
		return;
	}

	UIViewController* topPresentedVC = [self topPresentedVC];

	if ([modalToDismiss.options.animations.showModal hasCustomAnimation]) {
		modalToDismiss.transitioningDelegate = modalToDismiss;
	}

	if (modalToDismiss == topPresentedVC || [[topPresentedVC childViewControllers] containsObject:modalToDismiss]) {
		[modalToDismiss dismissViewControllerAnimated:modalToDismiss.options.animations.dismissModal.enable completion:^{
			[[_store pendingModalIdsToDismiss] removeObject:componentId];
			[_store removeComponent:componentId];
			[self removePendingNextModalIfOnTop];
		}];
	}
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
