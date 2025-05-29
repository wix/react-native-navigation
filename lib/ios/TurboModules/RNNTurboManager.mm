#ifdef RCT_NEW_ARCH_ENABLED
#import <Foundation/Foundation.h>
#import "RNNTurboManager.h"
#import "RNNCommandsHandler.h"
#import "RNNComponentViewCreator.h"
#import "RNNEventEmitter.h"
#import "RNNLayoutManager.h"
#import "RNNModalHostViewManagerHandler.h"
#import "RNNReactComponentRegistry.h"
#import "RNNReactRootViewCreator.h"
#import "RNNTurboCommandsHandler.h"
#import <React-RuntimeApple/ReactCommon/RCTHost.h>

@interface RNNTurboManager ()

@property(nonatomic, strong, readwrite) RNNExternalComponentStore *store;
@property(nonatomic, strong, readwrite) RNNReactComponentRegistry *componentRegistry;
@property(nonatomic, strong, readonly) RNNLayoutManager *layoutManager;
@property(nonatomic, strong, readonly) RNNOverlayManager *overlayManager;
@property(nonatomic, strong, readonly) RNNModalManager *modalManager;
@property(nonatomic, strong, readonly) RNNModalHostViewManagerHandler *modalHostViewHandler;
@property(nonatomic, strong, readonly) RNNCommandsHandler *commandsHandler;
@property(nonatomic, strong, readonly) RNNEventEmitter *eventEmitter;

@end

@implementation RNNTurboManager {
	UIWindow *_mainWindow;
}

- (instancetype)initWithHost:(RCTHost *)host mainWindow:(UIWindow *)mainWindow {
	if (self = [super init]) {
		_host = host;
		_mainWindow = mainWindow;
		_overlayManager = [RNNOverlayManager new];
		_store = [RNNExternalComponentStore new];

		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(onJavaScriptLoaded)
													 name:@"RCTInstanceDidLoadBundle"
												   object:nil];

		// TODO: investigate which new event is fired
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(onJavaScriptWillLoad)
													 name:RCTJavaScriptWillStartLoadingNotification
												   object:nil];

    _eventEmitter = [[RNNEventEmitter alloc] init];
		_eventEmitter.host = _host;

		RNNModalManagerEventHandler *modalManagerEventHandler =
			[[RNNModalManagerEventHandler alloc] initWithEventEmitter:_eventEmitter];

		_modalManager = [[RNNModalManager alloc] initWithHost:_host
												 eventHandler:modalManagerEventHandler];
		_modalHostViewHandler =
			[[RNNModalHostViewManagerHandler alloc] initWithModalManager:_modalManager];
		_layoutManager = [[RNNLayoutManager alloc] init];

		id<RNNComponentViewCreator> rootViewCreator =
			[[RNNReactRootViewCreator alloc] initWithHost:_host eventEmitter:_eventEmitter];

		_componentRegistry = [[RNNReactComponentRegistry alloc] initWithCreator:rootViewCreator];

		RNNViewControllerFactory *controllerFactory =
			[[RNNViewControllerFactory alloc] initWithRootViewCreator:rootViewCreator
													 eventEmitter:_eventEmitter
															store:_store
												componentRegistry:_componentRegistry
														  andHost:_host
									  bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];

		RNNSetRootAnimator *setRootAnimator = [RNNSetRootAnimator new];
		_commandsHandler = [[RNNCommandsHandler alloc] initWithViewControllerFactory:controllerFactory
																   layoutManager:_layoutManager
																	eventEmitter:_eventEmitter
																	modalManager:_modalManager
																  overlayManager:_overlayManager
																 setRootAnimator:setRootAnimator
																	  mainWindow:_mainWindow];

		[RNNTurboCommandsHandler setSharedInstance:_commandsHandler];
	}

	return self;
}

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalHostViewCreator)callback {
	[_store registerExternalHostComponent:name callback:callback];
}

- (UIViewController *)findComponentForId:(NSString *)componentId {
	return [_layoutManager findComponentForId:componentId];
}

- (void)onJavaScriptWillLoad {
	[_componentRegistry clear];
}

- (void)onJavaScriptLoaded {
  RCTExecuteOnMainQueue(^{
    UIApplication.sharedApplication.delegate.window.rootViewController = nil;
    
    [self->_commandsHandler setReadyToReceiveCommands:true];
    // TODO: Refactor
    //    [_modalHostViewHandler
    //        connectModalHostViewManager:[[_host moduleRegistry] moduleForName:"RCTModalHostViewManager"]];
    
    [self->_eventEmitter sendOnAppLaunched];
  });
}

@end
#endif
