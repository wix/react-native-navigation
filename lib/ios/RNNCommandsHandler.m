#import "RNNCommandsHandler.h"
#import "RNNModalManager.h"
#import "RNNNavigationStackManager.h"
#import "RNNOverlayManager.h"
#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"
#import "RNNSplitViewController.h"
#import "RNNElementFinder.h"
#import "React/RCTUIManager.h"


static NSString* const setRoot	= @"setRoot";
static NSString* const setStackRoot	= @"setStackRoot";
static NSString* const push	= @"push";
static NSString* const preview	= @"preview";
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
	[_eventEmitter sendOnNavigationCommand:setRoot params:@{@"layout": layout}];
	
	UIViewController *vc = [_controllerFactory createLayoutAndSaveToStore:layout[@"root"]];
	
	UIApplication.sharedApplication.delegate.window.rootViewController = vc;
	[UIApplication.sharedApplication.delegate.window makeKeyAndVisible];
	[_eventEmitter sendOnNavigationCommandCompletion:setRoot params:@{@"layout": layout}];
	completion();
}

-(void) mergeOptions:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationCommand:mergeOptions params:@{@"componentId": componentId, @"options": options}];
	
	UIViewController* vc = [_store findComponentForId:componentId];
	if([vc isKindOfClass:[RNNRootViewController class]]) {
		RNNRootViewController* rootVc = (RNNRootViewController*)vc;
		[rootVc.options mergeWith:options];
		[CATransaction begin];
		[CATransaction setCompletionBlock:completion];
		
		[rootVc.options applyOn:vc];
		
		[CATransaction commit];
	}

	if ([vc isKindOfClass:[RNNSplitViewController class]]) {
		RNNSplitViewController* splitVc = (RNNSplitViewController*)vc;
		[splitVc.options mergeWith:options];
		[CATransaction begin];
		[CATransaction setCompletionBlock:completion];
		
		[splitVc.options applyOn:vc];
		
		[CATransaction commit];
	}
	
}

-(void) setDefaultOptions:(NSDictionary*)optionsDict completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];
	[_eventEmitter sendOnNavigationCommand:setDefaultOptions params:@{@"options": optionsDict}];
	[_controllerFactory setDefaultOptionsDict:optionsDict];
}

-(void)push:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];

	UIViewController<RNNRootViewProtocol, UIViewControllerPreviewingDelegate> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];

	if (newVc.options.preview.elementId) {
		UIViewController* vc = [_store findComponentForId:componentId];

		if([vc isKindOfClass:[RNNRootViewController class]]) {
			RNNRootViewController* rootVc = (RNNRootViewController*)vc;
			rootVc.previewController = newVc;

			RNNElementFinder* elementFinder = [[RNNElementFinder alloc] initWithFromVC:vc];
			RNNElementView* elementView = [elementFinder findElementForId:newVc.options.preview.elementId];

			CGSize size = CGSizeMake(rootVc.view.frame.size.width, rootVc.view.frame.size.height);
			
			if (newVc.options.preview.width) {
				size.width = [newVc.options.preview.width floatValue];
			}

			if (newVc.options.preview.height) {
				size.height = [newVc.options.preview.height floatValue];
			}

			if (newVc.options.preview.width || newVc.options.preview.height) {
				newVc.preferredContentSize = size;
			}

			[rootVc registerForPreviewingWithDelegate:(id)rootVc sourceView:elementView];
		}
	} else {
		[_navigationStackManager push:newVc onTop:componentId completion:^{
			[_eventEmitter sendOnNavigationCommandCompletion:push params:@{@"componentId": componentId}];
			completion();
		} rejection:rejection];
	}
}

-(void)setStackRoot:(NSString*)componentId layout:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];
	
	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	__weak typeof(RNNEventEmitter*) weakEventEmitter = _eventEmitter;
	[_navigationStackManager setStackRoot:newVc fromComponent:componentId completion:^{
		[weakEventEmitter sendOnNavigationCommandCompletion:setStackRoot params:@{@"componentId": componentId}];
		completion();
	} rejection:rejection];
}

-(void)pop:(NSString*)componentId options:(NSDictionary*)options completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];

	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:pop params:@{@"componentId": componentId}];
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
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:popTo params:@{@"componentId": componentId}];
		completion();
	}];
	
	[_navigationStackManager popTo:componentId rejection:rejection];
	
	[CATransaction commit];
}

-(void) popToRoot:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion rejection:(RCTPromiseRejectBlock)rejection {
	[self assertReady];

	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:popToRoot params:@{@"componentId": componentId}];
		completion();
	}];
	
	[_navigationStackManager popToRoot:componentId rejection:rejection];
	
	[CATransaction commit];
}

-(void) showModal:(NSDictionary*)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];

	UIViewController<RNNRootViewProtocol> *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_modalManager showModal:newVc completion:^{
		[_eventEmitter sendOnNavigationCommandCompletion:showModal params:@{@"layout": layout}];
		completion();
	}];
}

-(void) dismissModal:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];

	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:dismissModal params:@{@"componentId": componentId}];
		completion();
	}];
	
	[_modalManager dismissModal:componentId];
	
	[CATransaction commit];
}

-(void) dismissAllModalsWithCompletion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];

	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[_eventEmitter sendOnNavigationCommandCompletion:dismissAllModals params:@{}];
		completion();
	}];
	
	[_modalManager dismissAllModals];
	
	[CATransaction commit];
}

-(void)showOverlay:(NSDictionary *)layout completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];

	UIViewController<RNNRootViewProtocol>* overlayVC = [_controllerFactory createOverlay:layout];
	[_overlayManager showOverlay:overlayVC completion:^{
		[_eventEmitter sendOnNavigationCommandCompletion:showOverlay params:@{@"layout": layout}];
		completion();
	}];
}

- (void)dismissOverlay:(NSString*)componentId completion:(RNNTransitionCompletionBlock)completion {
	[self assertReady];

	[_overlayManager dismissOverlay:componentId completion:^{
		[_eventEmitter sendOnNavigationCommandCompletion:dismissModal params:@{@"componentId": componentId}];
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
