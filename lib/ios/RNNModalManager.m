#import "RNNModalManager.h"
#import "RNNRootViewController.h"

@implementation RNNModalManager {
	RNNStore *_store;
	RNNTransitionWithComponentIdCompletionBlock _completionBlock;
}


-(instancetype)initWithStore:(RNNStore*)store {
	self = [super init];
	_store = store;
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

- (void)dismissModal:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion {
	[[_store pendingModalIdsToDismiss] addObject:componentId];
	[self removePendingNextModalIfOnTop:completion];
}

-(void)dismissAllModals {
	UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
	[root dismissViewControllerAnimated:YES completion:nil];
	[[_store pendingModalIdsToDismiss] removeAllObjects];
}

#pragma mark - private


-(void)removePendingNextModalIfOnTop:(RNNTransitionCompletionBlock)completion {
	NSString *componentId = [[_store pendingModalIdsToDismiss] lastObject];
	UIViewController<RNNRootViewProtocol> *modalToDismiss = (UIViewController<RNNRootViewProtocol>*)[_store findComponentForId:componentId];
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
			[[_store pendingModalIdsToDismiss] removeObject:componentId];
			[_store removeComponent:componentId];
			
			if (completion) {
				completion();
			}
			
			[self removePendingNextModalIfOnTop:nil];
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
