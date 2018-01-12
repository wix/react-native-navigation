#import "RNNNavigationStackManager.h"
#import "RNNRootViewController.h"
#import "RNNAnimator.h"


dispatch_queue_t RCTGetUIManagerQueue(void);
@implementation RNNNavigationStackManager {
	RNNStore *_store;
	RNNTransitionCompletionBlock _completionBlock;
}

-(instancetype)initWithStore:(RNNStore*)store {
	self = [super init];
	_store = store;
	return self;
}

-(void)push:(UIViewController<RNNRootViewProtocol> *)newTop onTop:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion {
	UIViewController *vc = [_store findComponentForId:componentId];
	[self preparePush:newTop onTopVC:vc completion:completion];
	[self waitForContentToAppearAndThen:@selector(pushAfterLoad:)];
}

-(void)preparePush:(UIViewController<RNNRootViewProtocol> *)newTop onTopVC:(UIViewController*)vc completion:(RNNTransitionCompletionBlock)completion {
	self.toVC = newTop;
	self.fromVC = vc;
	
	if (self.toVC.isCustomTransitioned) {
		vc.navigationController.delegate = newTop;
	} else {
		vc.navigationController.delegate = nil;
		self.fromVC.navigationController.interactivePopGestureRecognizer.delegate = nil;
	}
	
	_completionBlock = completion;
}

-(void)waitForContentToAppearAndThen:(SEL)nameOfSelector {
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:nameOfSelector
												 name: @"RCTContentDidAppearNotification"
											   object:nil];
}

-(void)pushAfterLoad:(NSDictionary*)notif {
	[[NSNotificationCenter defaultCenter] removeObserver:self name:@"RCTContentDidAppearNotification" object:nil];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		if (_completionBlock) {
			_completionBlock();
			_completionBlock = nil;
		}
	}];
	
	[[self.fromVC navigationController] pushViewController:self.toVC animated:YES];
	[CATransaction commit];
	
	self.toVC = nil;
	self.fromVC.navigationController.interactivePopGestureRecognizer.delegate = nil;
	self.fromVC = nil;
}

-(void)pop:(NSString *)componentId withAnimationData:(NSDictionary *)animationData {
	UIViewController* vc = [_store findComponentForId:componentId];
	UINavigationController* nvc = [vc navigationController];
	if ([nvc topViewController] == vc) {
		if (animationData) {
			RNNRootViewController* RNNVC = (RNNRootViewController*)vc;
			nvc.delegate = RNNVC;
			[RNNVC.animator setupTransition:animationData];
			[nvc popViewControllerAnimated:YES];
		} else {
			nvc.delegate = nil;
			[nvc popViewControllerAnimated:YES];
		}
	} else {
		NSMutableArray * vcs = nvc.viewControllers.mutableCopy;
		[vcs removeObject:vc];
		[nvc setViewControllers:vcs animated:YES];
	}
	[_store removeComponent:componentId];
}

-(void)popTo:(NSString*)componentId {
	UIViewController *vc = [_store findComponentForId:componentId];
	
	if (vc) {
		UINavigationController *nvc = [vc navigationController];
		if(nvc) {
			NSArray *poppedVCs = [nvc popToViewController:vc animated:YES];
			[self removePopedViewControllers:poppedVCs];
		}
	}
}

-(void) popToRoot:(NSString*)componentId {
	UIViewController* vc = [_store findComponentForId:componentId];
	UINavigationController* nvc = [vc navigationController];
	NSArray* poppedVCs = [nvc popToRootViewControllerAnimated:YES];
	[self removePopedViewControllers:poppedVCs];
}

-(void)removePopedViewControllers:(NSArray*)viewControllers {
	for (UIViewController *popedVC in viewControllers) {
		[_store removeComponentByViewControllerInstance:popedVC];
	}
}

@end
