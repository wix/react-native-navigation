#import "RNNNavigationStackManager.h"
#import "RNNRootViewController.h"
#import "RNNAnimator.h"
#import "RNNErrorHandler.h"


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

-(void)push:(UIViewController<RNNRootViewProtocol> *)newTop onTop:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	UIViewController *vc = [_store findComponentForId:componentId];
	[self preparePush:newTop onTopVC:vc completion:completion];
	[self assertNavigationControllerExist:vc reject:rejection];
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

-(void)pop:(NSString *)componentId withTransitionOptions:(RNNAnimationOptions *)transitionOptions rejection:(RCTPromiseRejectBlock)rejection {
	RNNRootViewController* vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	[self assertNavigationControllerExist:vc reject:rejection];
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

-(void)popTo:(NSString*)componentId rejection:(RCTPromiseRejectBlock)rejection {
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	[self assertNavigationControllerExist:vc reject:rejection];
	if (vc) {
		UINavigationController *nvc = [vc navigationController];
		if(nvc) {
			NSArray *poppedVCs = [nvc popToViewController:vc animated:vc.options.animations.pop.enable];
			[self removePopedViewControllers:poppedVCs];
		}
	}
}

-(void)popToRoot:(NSString*)componentId rejection:(RCTPromiseRejectBlock)rejection {
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	[self assertNavigationControllerExist:vc reject:rejection];
	UINavigationController* nvc = [vc navigationController];
	NSArray* poppedVCs = [nvc popToRootViewControllerAnimated:vc.options.animations.pop.enable];
	[self removePopedViewControllers:poppedVCs];
}

-(void)setStackRoot:(UIViewController<RNNRootViewProtocol> *)newRoot fromComponent:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	UIViewController* vc = [_store findComponentForId:componentId];
	[self assertNavigationControllerExist:vc reject:rejection];
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

- (void)assertNavigationControllerExist:(UIViewController *)viewController reject:(RCTPromiseRejectBlock)reject {
	if (!viewController.navigationController) {
		_completionBlock = nil;
		[RNNErrorHandler reject:reject withErrorCode:RNNCommandErrorCodeNoStack errorDescription:[NSString stringWithFormat:@"%@ should be called from a stack child component", [RNNErrorHandler getCallerFunctionName]]];
	}
}

-(void)removePopedViewControllers:(NSArray*)viewControllers {
	for (UIViewController *popedVC in viewControllers) {
		[_store removeComponentByViewControllerInstance:popedVC];
	}
}

@end
