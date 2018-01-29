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
-(void)waitForContentToAppearAndThen:(SEL)nameOfSelector {
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:nameOfSelector
												 name: @"RCTContentDidAppearNotification"
											   object:nil];
}

-(void)showModalAfterLoad:(NSDictionary*)notif {
	[[NSNotificationCenter defaultCenter] removeObserver:self name:@"RCTContentDidAppearNotification" object:nil];
	UIViewController *topVC = [self topPresentedVC];
	[topVC presentViewController:self.toVC animated:self.toVC.isAnimated completion:^{
		if (_completionBlock) {
			_completionBlock();
			_completionBlock = nil;
		}
	}];
}

-(void)showModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	self.toVC = (UIViewController<RNNRootViewProtocol>*)viewController;
	_completionBlock = completion;
	[self waitForContentToAppearAndThen:@selector(showModalAfterLoad:)];
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
	
	if (modalToDismiss == topPresentedVC || [[topPresentedVC childViewControllers] containsObject:modalToDismiss]) {
		[modalToDismiss dismissViewControllerAnimated:modalToDismiss.isAnimated completion:^{
			[[_store pendingModalIdsToDismiss] removeObject:componentId];
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


@end
