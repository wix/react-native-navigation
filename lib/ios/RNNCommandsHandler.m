#import "RNNCommandsHandler.h"
#import "RNNModalManager.h"
#import "RNNNavigationStackManager.h"
#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "React/RCTUIManager.h"

@implementation RNNCommandsHandler {
	RNNControllerFactory *_controllerFactory;
	RNNStore *_store;
	RNNNavigationStackManager* _navigationStackManager;
	RNNModalManager* _modalManager;
}

-(instancetype) initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory {
	self = [super init];
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

-(void) setOptions:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
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
	RNNNavigationOptions* options = [[RNNNavigationOptions alloc] initWithDict:optionsDict];
	[_controllerFactory setDefaultOptions:options];
}

-(void) push:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_navigationStackManager push:newVc onTop:componentId completion:completion];
}

-(void)pop:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	NSDictionary* animationData = options[@"customTransition"];
	if (animationData){
		if ([animationData objectForKey:@"animations"]) {
			[_navigationStackManager pop:componentId withAnimationData:animationData];
		} else {
			[[NSException exceptionWithName:NSInvalidArgumentException reason:@"unsupported transitionAnimation" userInfo:nil] raise];
		}
	} else {
		[_navigationStackManager pop:componentId withAnimationData:nil];
	}
	[CATransaction commit];
}

-(void) popTo:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	
	[_navigationStackManager popTo:componentId];
	
	[CATransaction commit];
}

-(void) popToRoot:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	
	[_navigationStackManager popToRoot:componentId];
	
	[CATransaction commit];
}

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	UIViewController *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_modalManager showModal:newVc completion:completion];
}

-(void) dismissModal:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[CATransaction begin];
	[CATransaction setCompletionBlock:completion];
	
	[_modalManager dismissModal:componentId];
	
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
