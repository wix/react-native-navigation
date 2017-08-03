
#import "RNNCommandsHandler.h"

#import "RNNModalManager.h"
#import "RNNNavigationStackManager.h"
#import "RNNNavigationOptions.h"
#import "RNNRootViewController.h"

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

-(void) setRoot:(NSDictionary*)layout {
	[self assertReady];

	[_modalManager dismissAllModals];
	
	UIViewController *vc = [_controllerFactory createLayoutAndSaveToStore:layout];
	
	UIApplication.sharedApplication.delegate.window.rootViewController = vc;
	[UIApplication.sharedApplication.delegate.window makeKeyAndVisible];
}

-(void) setOptions:(NSString*)containerId options:(NSDictionary*)options {
	[self assertReady];
	
	UIViewController* vc = [_store findContainerForId:containerId];
	if([vc isKindOfClass:[RNNRootViewController class]]) {
		RNNRootViewController* rootVc = (RNNRootViewController*)vc;
		[rootVc.navigationOptions mergeWith:options];
		[rootVc.navigationOptions applyOn:vc];
	}
}

-(void) push:(NSString*)containerId layout:(NSDictionary*)layout {
	[self assertReady];
	
	UIViewController *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_navigationStackManager push:newVc onTop:containerId];
}

-(void) pop:(NSString*)containerId {
	[self assertReady];
	
	[_navigationStackManager pop:containerId];
}

-(void) popTo:(NSString*)containerId {
	[self assertReady];
	
	[_navigationStackManager popTo:containerId];
}

-(void) popToRoot:(NSString*)containerId {
	[self assertReady];
	
	[_navigationStackManager popToRoot:containerId];
}

-(void) showModal:(NSDictionary*)layout {
	[self assertReady];
	
	UIViewController *newVc = [_controllerFactory createLayoutAndSaveToStore:layout];
	[_modalManager showModal:newVc];
}

-(void) dismissModal:(NSString*)containerId {
	[self assertReady];
	
	[_modalManager dismissModal:containerId];
}

-(void) dismissAllModals {
	[self assertReady];
	
	[_modalManager dismissAllModals];
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
