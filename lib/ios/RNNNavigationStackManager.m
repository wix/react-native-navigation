#import "RNNNavigationStackManager.h"
#import "RNNRootViewController.h"
#import "RNNAnimator.h"
#import "RNNErrorHandler.h"
#import "RNNNavigationController.h"

dispatch_queue_t RCTGetUIManagerQueue(void);
@interface RNNNavigationStackManager()
@property (nonatomic, strong) RNNNavigationController* nvc;
@property (nonatomic, strong) RNNRootViewController* toVC;
@end

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
	RNNNavigationController *nvc = [self getComponentStack:componentId];
	[self assertNavigationControllerExist:nvc reject:rejection];
	[self preparePush:newTop onTopVC:nvc completion:completion];
	if ([newTop isCustomViewController]) {
		[self pushAfterLoad:nil];
	} else {
		[self waitForContentToAppearAndThen:@selector(pushAfterLoad:)];
	}
}

-(void)preparePush:(UIViewController<RNNRootViewProtocol> *)newTop onTopVC:(RNNNavigationController*)nvc completion:(RNNTransitionCompletionBlock)completion {
	self.toVC = (RNNRootViewController*)newTop;
	self.nvc = nvc;
	
	
	if (self.toVC.options.animations.push.hasCustomAnimation || self.toVC.isCustomTransitioned) {
		nvc.delegate = newTop;
	} else {
		nvc.delegate = nil;
		nvc.interactivePopGestureRecognizer.delegate = nil;
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
	
	[self.nvc pushViewController:self.toVC animated:self.toVC.options.animations.push.enable];
	[CATransaction commit];
	
	self.toVC = nil;
	self.nvc.interactivePopGestureRecognizer.delegate = nil;
	self.nvc = nil;
}

-(void)pop:(NSString *)componentId withTransitionOptions:(RNNAnimationOptions *)transitionOptions rejection:(RCTPromiseRejectBlock)rejection {
	RNNRootViewController* vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	UINavigationController* nvc = [self getComponentStack:componentId];
	[self assertNavigationControllerExist:nvc reject:rejection];
	
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
	RNNNavigationController *nvc = [self getComponentStack:componentId];
	[self assertNavigationControllerExist:nvc reject:rejection];
	
	if (vc && nvc) {
		NSArray *poppedVCs = [nvc popToViewController:vc animated:vc.options.animations.pop.enable];
		[self removePopedViewControllers:poppedVCs];
	}
}

-(void)popToRoot:(NSString*)componentId rejection:(RCTPromiseRejectBlock)rejection {
	RNNRootViewController *vc = (RNNRootViewController*)[_store findComponentForId:componentId];
	RNNNavigationController *nvc = [self getComponentStack:componentId];
	[self assertNavigationControllerExist:nvc reject:rejection];
	NSArray* poppedVCs = [nvc popToRootViewControllerAnimated:vc.options.animations.pop.enable];
	[self removePopedViewControllers:poppedVCs];
}

-(void)setStackRoot:(UIViewController<RNNRootViewProtocol> *)newRoot fromComponent:(NSString *)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	RNNNavigationController* nvc = [self getComponentStack:componentId];

	[self assertNavigationControllerExist:nvc reject:rejection];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		if (completion) {
			completion();
		}
	}];
	
	[nvc setViewControllers:@[newRoot] animated:newRoot.options.animations.push.enable];
	
	[CATransaction commit];
}

- (void)assertNavigationControllerExist:(UINavigationController *)viewController reject:(RCTPromiseRejectBlock)reject {
	if (![viewController isKindOfClass:[UINavigationController class]]) {
		_completionBlock = nil;
		[RNNErrorHandler reject:reject withErrorCode:RNNCommandErrorCodeNoStack errorDescription:[NSString stringWithFormat:@"%@ should be called from a stack child component", [RNNErrorHandler getCallerFunctionName]]];
	}
}

- (RNNNavigationController*)getComponentStack:(NSString *)componentId {
	UIViewController* component = [_store findComponentForId:componentId];
	if ([component isKindOfClass:[UINavigationController class]]) {
		return (RNNNavigationController*)component;
	} else {
		return (RNNNavigationController*)component.navigationController;
	}
}

-(void)removePopedViewControllers:(NSArray*)viewControllers {
	for (UIViewController *popedVC in viewControllers) {
		[_store removeComponentByViewControllerInstance:popedVC];
	}
}

@end
