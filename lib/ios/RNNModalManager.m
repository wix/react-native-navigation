#import "RNNModalManager.h"
#import "RNNRootViewController.h"

@implementation RNNModalManager {
	RNNStore *_store;
}


-(instancetype)initWithStore:(RNNStore*)store {
	self = [super init];
	_store = store;
	return self;
}

-(void)showModal:(UIViewController *)viewController {
	UIViewController *topVC = [self topPresentedVC];
	[topVC presentViewController:viewController animated:YES completion:nil];
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
