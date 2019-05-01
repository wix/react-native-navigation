#import "RNNBridgeManager.h"

#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

#import "RNNEventEmitter.h"
#import "RNNSplashScreen.h"
#import "RNNBridgeModule.h"
#import "RNNRootViewCreator.h"
#import "RNNReactRootViewCreator.h"
#import "RNNReactComponentRegistry.h"

@interface RNNBridgeManager() <RCTBridgeDelegate>

@property (nonatomic, strong, readwrite) RCTBridge *bridge;
@property (nonatomic, strong, readwrite) RNNExternalComponentStore *store;
@property (nonatomic, strong, readwrite) RNNReactComponentRegistry *componentRegistry;

@end

@implementation RNNBridgeManager {
	NSURL* _jsCodeLocation;
	NSDictionary* _launchOptions;
	id<RNNBridgeManagerDelegate> _delegate;
	RCTBridge* _bridge;
	UIWindow* _mainWindow;
	
	RNNExternalComponentStore* _store;

	RNNCommandsHandler* _commandsHandler;
}

- (instancetype)initWithJsCodeLocation:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions bridgeManagerDelegate:(id<RNNBridgeManagerDelegate>)delegate mainWindow:(UIWindow *)mainWindow {
	if (self = [super init]) {
		_mainWindow = mainWindow;
		_jsCodeLocation = jsCodeLocation;
		_launchOptions = launchOptions;
		_delegate = delegate;
		
		_store = [RNNExternalComponentStore new];
		_bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:_launchOptions];
		
		
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(onJavaScriptLoaded)
													 name:RCTJavaScriptDidLoadNotification
												   object:nil];
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(onJavaScriptWillLoad)
													 name:RCTJavaScriptWillStartLoadingNotification
												   object:nil];
		[[NSNotificationCenter defaultCenter] addObserver:self
												 selector:@selector(onBridgeWillReload)
													 name:RCTBridgeWillReloadNotification
												   object:nil];
	}
	return self;
}

- (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
	[_store registerExternalComponent:name callback:callback];
}

- (NSArray *)extraModulesFromDelegate {
	if ([_delegate respondsToSelector:@selector(extraModulesForBridge:)]) {
		return [_delegate extraModulesForBridge:_bridge];
	}
	
	return nil;
}

# pragma mark - RCTBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
	return _jsCodeLocation;
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
	RNNEventEmitter *eventEmitter = [[RNNEventEmitter alloc] init];

	id<RNNRootViewCreator> rootViewCreator = [[RNNReactRootViewCreator alloc] initWithBridge:bridge];
	_componentRegistry = [[RNNReactComponentRegistry alloc] initWithCreator:rootViewCreator];
	RNNControllerFactory *controllerFactory = [[RNNControllerFactory alloc] initWithRootViewCreator:rootViewCreator eventEmitter:eventEmitter store:_store componentRegistry:_componentRegistry andBridge:bridge];
	
	_commandsHandler = [[RNNCommandsHandler alloc] initWithControllerFactory:controllerFactory eventEmitter:eventEmitter stackManager:[RNNNavigationStackManager new] modalManager:[RNNModalManager new] overlayManager:[RNNOverlayManager new] mainWindow:_mainWindow];
	RNNBridgeModule *bridgeModule = [[RNNBridgeModule alloc] initWithCommandsHandler:_commandsHandler];

	return [@[bridgeModule,eventEmitter] arrayByAddingObjectsFromArray:[self extraModulesFromDelegate]];
}

# pragma mark - JavaScript & Bridge Notifications

- (void)onJavaScriptWillLoad {
	[_componentRegistry clear];
}

- (void)onJavaScriptLoaded {
	[_commandsHandler setReadyToReceiveCommands:true];
	[[_bridge moduleForClass:[RNNEventEmitter class]] sendOnAppLaunched];
}

- (void)onBridgeWillReload {
	UIApplication.sharedApplication.delegate.window.rootViewController =  nil;
}

@end

