#import "RNNCommandsHandler.h"
#import "RNNModalManager.h"
#import "RNNNavigationStackManager.h"
#import "RNNOverlayManager.h"
#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "React/RCTUIManager.h"

@implementation RNNCommandsHandler {
	RNNControllerFactory *_controllerFactory;
	RNNStore *_store;
	RNNNavigationStackManager* _navigationStackManager;
	RNNModalManager* _modalManager;
	RNNOverlayManager* _overlayManager;
}

-(instancetype) initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory {
	self = [super init];
	_store = store;
	_controllerFactory = controllerFactory;
	_navigationStackManager = [[RNNNavigationStackManager alloc] initWithStore:_store];
	_modalManager = [[RNNModalManager alloc] initWithStore:_store];
	_overlayManager = [[RNNOverlayManager alloc] initWithStore:_store];
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

-(void) mergeOptions:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController* vc = [_store findComponentForId:componentId];
	if([vc isKindOfClass:[RNNRootViewController class]]) {
		RNNRootViewController* rootVc = (RNNRootViewController*)vc;
		[rootVc.options mergeWith:options];
		[CATransaction begin];
		[CATransaction setCompletionBlock:completion];
		
		[rootVc.options applyOn:vc];
		
		[CATransaction commit];
	}
}

-(void) setDefaultOptions:(NSDictionary*)optionsDict completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_controllerFactory setDefaultOptionsDict:optionsDict];
}

-(void)push:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_navigationStackManager push:newVc onTop:componentId completion:^{
		completion();
	}];
}

-(void)setStackRoot:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_navigationStackManager setRoot:newVc fromComponent:componentId completion:^{
		completion();
	}];
}

-(void)pop:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	NSDictionary* animationData = options[@"customTransition"];
	RNNAnimationOptions* transitionOptions = [[RNNAnimationOptions alloc] initWithDict:animationData];
	
	if (transitionOptions.animations){
		[_navigationStackManager pop:componentId withTransitionOptions:transitionOptions];
	} else {
		[_navigationStackManager pop:componentId withTransitionOptions:nil];
	}
	[CATransaction commit];
}

-(void) popTo:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_navigationStackManager popTo:componentId];
	
	[CATransaction commit];
}

-(void) popToRoot:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_navigationStackManager popToRoot:componentId];
	
	[CATransaction commit];
}

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_modalManager showModal:newVc completion:^{
		completion();
	}];
}

-(void) dismissModal:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_modalManager dismissModal:componentId];
	
	[CATransaction commit];
}

-(void) dismissAllModalsWithCompletion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_modalManager dismissAllModals];
	
	[CATransaction commit];
}

-(void)showOverlay:(NSDictionary *)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	UIViewController<RNNRootViewProtocol>* overlayVC = [_controllerFactory createOverlay:layout];
	[_overlayManager showOverlay:overlayVC completion:^{
		completion();
	}];
}

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_overlayManager dismissOverlay:componentId completion:^{	
		completion();
	}];
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
