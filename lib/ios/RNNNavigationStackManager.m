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
	if ([newTop isCustomViewController]) {
		[self pushAfterLoad:nil];
	} else {
		[self waitForContentToAppearAndThen:@selector(pushAfterLoad:)];
	}
}

-(void)preparePush:(UIViewController<RNNRootViewProtocol> *)newTop onTopVC:(UIViewController*)vc completion:(RNNTransitionCompletionBlock)completion {
	self.toVC = (RNNRootViewController*)newTop;
	self.fromVC = vc;
	
	
	if (self.toVC.options.animations.push.hasCustomAnimation || self.toVC.isCustomTransitioned) {
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
	
	[[self.fromVC navigationController] pushViewController:self.toVC animated:self.toVC.options.animations.push.enable];
	[CATransaction commit];
	
	self.toVC = nil;
	self.fromVC.navigationController.interactivePopGestureRecognizer.delegate = nil;
	self.fromVC = nil;
}

-(void)pop:(NSString *)componentId withTransitionOptions:(RNNAnimationOptions *)transitionOptions {
	RNNRootViewController* vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	UINavigationController* nvc = [vc navigationController];
	if ([nvc topViewController] == vc) {
		if (vc.options.animations.pop) {
			nvc.delegate = vc;
			[nvc popViewControllerAnimated:vc.options.animations.pop.enable];
		} else {
			nvc.delegate = nil;
			[nvc popViewControllerAnimated:vc.options.animations.pop.enable];
		}
	} else {
		NSMutableArray * vcs = nvc.viewControllers.mutableCopy;
		[vcs removeObject:vc];
		[nvc setViewControllers:vcs animated:vc.options.animations.pop.enable];
	}
	[_store removeComponent:componentId];
}

-(void)popTo:(NSString*)componentId {
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	
	if (vc) {
		UINavigationController *nvc = [vc navigationController];
		if(nvc) {
			NSArray *poppedVCs = [nvc popToViewController:vc animated:vc.options.animations.pop.enable];
			[self removePopedViewControllers:poppedVCs];
		}
	}
}

-(void)popToRoot:(NSString*)componentId {
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	UINavigationController* nvc = [vc navigationController];
	NSArray* poppedVCs = [nvc popToRootViewControllerAnimated:vc.options.animations.pop.enable];
	[self removePopedViewControllers:poppedVCs];
}

-(void)setRoot:(UIViewController<RNNRootViewProtocol> *)newRoot fromComponent:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion {
	UIViewController* vc = [_store findComponentForId:componentId];
	UINavigationController* nvc = [vc navigationController];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		if (completion) {
			completion();
		}
	}];
	
	[nvc setViewControllers:@[newRoot] animated:newRoot.options.animations.push.enable];
	
	[CATransaction commit];
}

-(void)removePopedViewControllers:(NSArray*)viewControllers {
	for (UIViewController *popedVC in viewControllers) {
		[_store removeComponentByViewControllerInstance:popedVC];
	}
}

@end
