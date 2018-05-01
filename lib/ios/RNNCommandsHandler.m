#import "RNNCommandsHandler.h"
#import "RNNModalManager.h"
#import "RNNNavigationStackManager.h"
#import "RNNOverlayManager.h"
#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "React/RCTUIManager.h"

static NSString* const setRoot	= @"setRoot";
static NSString* const setStackRoot	= @"setStackRoot";
static NSString* const push	= @"push";
static NSString* const pop	= @"pop";
static NSString* const popTo	= @"popTo";
static NSString* const popToRoot	= @"popToRoot";
static NSString* const showModal	= @"showModal";
static NSString* const dismissModal	= @"dismissModal";
static NSString* const dismissAllModals	= @"dismissAllModals";
static NSString* const showOverlay	= @"showOverlay";
static NSString* const dismissOverlay	= @"dismissOverlay";
static NSString* const mergeOptions	= @"mergeOptions";
static NSString* const setDefaultOptions	= @"setDefaultOptions";

@implementation RNNCommandsHandler {
	RNNControllerFactory *_controllerFactory;
	RNNStore *_store;
	RNNNavigationStackManager* _navigationStackManager;
	RNNModalManager* _modalManager;
	RNNOverlayManager* _overlayManager;
	RNNEventEmitter* _eventEmitter;
}

-(instancetype) initWithStore:(RNNStore*)store controllerFactory:(RNNControllerFactory*)controllerFactory eventEmitter:(RNNEventEmitter *)eventEmitter {
	self = [super init];
	_store = store;
	_controllerFactory = controllerFactory;
	_eventEmitter = eventEmitter;
	_navigationStackManager = [[RNNNavigationStackManager alloc] initWithStore:_store];
	_modalManager = [[RNNModalManager alloc] initWithStore:_store];
	_overlayManager = [[RNNOverlayManager alloc] initWithStore:_store];
	return self;
}

#pragma mark - public

-(void) setRoot:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	
	[_modalManager dismissAllModals];
	[_eventEmitter sendOnNavigationComment:setRoot params:@{@"layout": layout}];

	UIViewController *vc = [_controllerFactory createLayoutAndSaveToStore:layout];
	
	UIApplication.sharedApplication.delegate.window.rootViewController = vc;
	[UIApplication.sharedApplication.delegate.window makeKeyAndVisible];
	completion();
}

-(void) mergeOptions:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:mergeOptions params:@{@"componentId": componentId, @"options": options}];

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
	[_eventEmitter sendOnNavigationComment:setDefaultOptions params:@{@"options": optionsDict}];
	[_controllerFactory setDefaultOptionsDict:optionsDict];
}

-(void)push:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:push params:@{@"componentId": componentId}];
	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_navigationStackManager push:newVc onTop:componentId completion:^{
		completion();
	} rejection:rejection];
}

-(void)setStackRoot:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:setStackRoot params:@{@"componentId": componentId}];

	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_navigationStackManager setStackRoot:newVc fromComponent:componentId completion:^{
		completion();
	} rejection:rejection];
}

-(void)pop:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:pop params:@{@"componentId": componentId}];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	NSDictionary* animationData = options[@"customTransition"];
	RNNAnimationOptions* transitionOptions = [[RNNAnimationOptions alloc] initWithDict:animationData];
	
	if (transitionOptions.animations){
		[_navigationStackManager pop:componentId withTransitionOptions:transitionOptions rejection:rejection];
	} else {
		[_navigationStackManager pop:componentId withTransitionOptions:nil rejection:rejection];
	}
	[CATransaction commit];
}

-(void) popTo:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:popTo params:@{@"componentId": componentId}];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_navigationStackManager popTo:componentId rejection:rejection];
	
	[CATransaction commit];
}

-(void) popToRoot:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:popToRoot params:@{@"componentId": componentId}];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_navigationStackManager popToRoot:componentId rejection:rejection];
	
	[CATransaction commit];
}

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:showModal params:@{@"layout": layout}];
	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_modalManager showModal:newVc completion:^{
		completion();
	}];
}

-(void) dismissModal:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:dismissModal params:@{@"componentId": componentId}];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_modalManager dismissModal:componentId];
	
	[CATransaction commit];
}

-(void) dismissAllModalsWithCompletion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:dismissAllModals params:@{}];
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		completion();
	}];
	
	[_modalManager dismissAllModals];
	
	[CATransaction commit];
}

-(void)showOverlay:(NSDictionary *)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:showOverlay params:@{@"layout": layout}];
	UIViewController<RNNRootViewProtocol>* overlayVC = [_controllerFactory createOverlay:layout];
	[_overlayManager showOverlay:overlayVC completion:^{
		completion();
	}];
}

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationComment:dismissModal params:@{@"componentId": componentId}];
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
