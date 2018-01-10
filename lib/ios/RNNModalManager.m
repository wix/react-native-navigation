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
	[topVC presentViewController:self.toVC animated:YES completion:^{
		if (_completionBlock) {
			_completionBlock();
			_completionBlock = nil;
		}
	}];
}

-(void)showModal:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	self.toVC = viewController;
	_completionBlock = completion;
	[self waitForContentToAppearAndThen:@selector(showModalAfterLoad:)];
}

-(void)dismissModal:(NSString *)containerId {
	[[_store pendingModalIdsToDismiss] addObject:containerId];
	[self removePendingNextModalIfOnTop];
}

-(void)dismissAllModals {
	UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
	[root dismissViewControllerAnimated:YES completion:nil];
	[[_store pendingModalIdsToDismiss] removeAllObjects];
}

#pragma mark - private


-(void)removePendingNextModalIfOnTop {
	NSString *containerId = [[_store pendingModalIdsToDismiss] lastObject];
	
	UIViewController *modalToDismiss = [_store findContainerForId:containerId];
	
	if(!modalToDismiss) {
		return;
	}
	
	UIViewController* topPresentedVC = [self topPresentedVC];
	
	if (modalToDismiss == topPresentedVC || [[topPresentedVC childViewControllers] containsObject:modalToDismiss]) {
		[modalToDismiss dismissViewControllerAnimated:YES completion:^{
			[[_store pendingModalIdsToDismiss] removeObject:containerId];
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
