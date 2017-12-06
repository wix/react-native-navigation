#import "RNNNavigationStackManager.h"
#import "RNNRootViewController.h"
#import "React/RCTUIManager.h"
#import "RNNAnimator.h"


dispatch_queue_t RCTGetUIManagerQueue(void);
@implementation RNNNavigationStackManager {
	RNNStore *_store;
}

-(instancetype)initWithStore:(RNNStore*)store {
	self = [super init];
	_store = store;
	return self;
}


-(void)push:(UIViewController *)newTop onTop:(NSString *)containerId customAnimationData:(NSDictionary*)customAnimationData bridge:(RCTBridge*)bridge {
	UIViewController *vc = [_store findContainerForId:containerId];
	[self preparePush:newTop onTopVC:vc customAnimationData:customAnimationData bridge:bridge];
	[self waitForContentToAppearAndThen:@selector(pushAfterLoad:)];
}

-(void)preparePush:(UIViewController *)newTop onTopVC:(UIViewController*)vc customAnimationData:(NSDictionary*)customAnimationData bridge:(RCTBridge*)bridge {
	if (customAnimationData) {
		RNNRootViewController* newTopRootView = (RNNRootViewController*)newTop;
		self.fromVC = vc;
		self.toVC = newTopRootView;
		vc.navigationController.delegate = newTopRootView;
		[newTopRootView.animator setupTransition:customAnimationData];
		RCTUIManager *uiManager = bridge.uiManager;
		CGRect screenBound = [vc.view bounds];
		CGSize screenSize = screenBound.size;
		[uiManager setAvailableSize:screenSize forRootView:self.toVC.view];
	} else {
		self.fromVC = vc;
		self.toVC = (RNNRootViewController*)newTop;
		vc.navigationController.delegate = nil;
		self.fromVC.navigationController.interactivePopGestureRecognizer.delegate = nil;
	}
}

-(void)waitForContentToAppearAndThen:(SEL)nameOfSelector {
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:nameOfSelector
												 name: @"RCTContentDidAppearNotification"
											   object:nil];
}

-(void)pushAfterLoad:(NSDictionary*)notif {
	[[NSNotificationCenter defaultCenter] removeObserver:self name:@"RCTContentDidAppearNotification" object:nil];
	[[self.fromVC navigationController] pushViewController:self.toVC animated:YES];
	self.toVC = nil;
	self.fromVC.navigationController.interactivePopGestureRecognizer.delegate = nil;
	self.fromVC = nil;
}

-(void)pop:(NSString *)containerId withAnimationData:(NSDictionary *)animationData {
	UIViewController* vc = [_store findContainerForId:containerId];
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
	[_store removeContainer:containerId];
}

-(void)popTo:(NSString*)containerId {
	UIViewController *vc = [_store findContainerForId:containerId];
	
	if (vc) {
		UINavigationController *nvc = [vc navigationController];
		if(nvc) {
			NSArray *poppedVCs = [nvc popToViewController:vc animated:YES];
			[self removePopedViewControllers:poppedVCs];
		}
	}
}

-(void) popToRoot:(NSString*)containerId {
	UIViewController* vc = [_store findContainerForId:containerId];
	UINavigationController* nvc = [vc navigationController];
	NSArray* poppedVCs = [nvc popToRootViewControllerAnimated:YES];
	[self removePopedViewControllers:poppedVCs];
}

-(void)removePopedViewControllers:(NSArray*)viewControllers {
	for (UIViewController *popedVC in viewControllers) {
		[_store removeContainerByViewControllerInstance:popedVC];
	}
}

@end
