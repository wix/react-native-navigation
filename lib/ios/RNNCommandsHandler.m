#import "RNNCommandsHandler.h"
#import "RNNModalManager.h"
#import "RNNNavigationStackManager.h"
#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "React/RCTUIManager.h"

@implementation RNNCommandsHandler {
	RNNControllerFactory *_controllerFactory;
	RNNStore *_store;
	RCTBridge* _bridge;
	RNNNavigationStackManager* _navigationStackManager;
	RNNModalManager* _modalManager;
}

-(instancetype) initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory andBridge:(RCTBridge*)bridge {
	self = [super init];
	_bridge = bridge;
	_store = store;
	_controllerFactory = controllerFactory;
	_navigationStackManager = [[RNNNavigationStackManager alloc] initWithStore:_store];
	_modalManager = [[RNNModalManager alloc] initWithStore:_store];
	return self;
}

#pragma mark - public

-(void) setRoot:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	[_modalManager dismissAllModals];
	
	UIViewController *vc = [_controllerFactory createLayoutAndSaveToStore:layout];
	
	UIApplication.sharedApplication.delegate.window.rootViewController = vc;
	[UIApplication.sharedApplication.delegate.window makeKeyAndVisible];
	completion();
}

-(void) setOptions:(NSString*)containerId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController* vc = [_store findContainerForId:containerId];
	if([vc isKindOfClass:[RNNRootViewController class]]) {
		RNNRootViewController* rootVc = (RNNRootViewController*)vc;
		[rootVc.navigationOptions mergeWith:options];
		[CATransaction begin];
		[CATransaction setCompletionBlock:completion];
		
		[rootVc.navigationOptions applyOn:vc];
		[rootVc applyNavigationButtons];
		
		[CATransaction commit];
	}
}

-(void) push:(NSString*)containerId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	UIViewController *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	UIViewController *fromVc = [_store findContainerForId:containerId];
	[_bridge.uiManager setAvailableSize:fromVc.view.bounds.size forRootView:newVc.view];
	[_navigationStackManager push:newVc onTop:containerId completion:completion];
}

-(void)pop:(NSString*)containerId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	NSDictionary* animationData = options[@"customTransition"];
	if (animationData){
		if ([animationData objectForKey:@"animations"]) {
			[_navigationStackManager pop:containerId withAnimationData:animationData];
		} else {
			[[NSException exceptionWithName:NSInvalidArgumentException reason:@"unsupported transitionAnimation" userInfo:nil] raise];
		}
	} else {
		[_navigationStackManager pop:containerId withAnimationData:nil];
	}
	[CATransaction commit];
}

-(void) popTo:(NSString*)containerId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	
	[_navigationStackManager popTo:containerId];
	
	[CATransaction commit];
}

-(void) popToRoot:(NSString*)containerId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	
	[_navigationStackManager popToRoot:containerId];
	
	[CATransaction commit];
}

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_modalManager showModal:newVc completion:completion];
}

-(void) dismissModal:(NSString*)containerId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	
	[_modalManager dismissModal:containerId];
	
	[CATransaction commit];
}

-(void) dismissAllModalsWithCompletion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	
	[_modalManager dismissAllModals];
	
	[CATransaction commit];
}

#pragma mark - private

-(void) assertReady {
	if (!_store.isReadyToReceiveCommands) {
		[[NSException exceptionWithName:@"BridgeNotLoadedError"
								reason:@"Bridge not yet loaded! Send commands after Navigation.events().onAppLaunched() has been called."
							  userInfo:nil]
		 raise];
	}
}

@end
