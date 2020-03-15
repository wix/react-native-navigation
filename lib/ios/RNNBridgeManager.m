#import "RNNBridgeManager.h"

#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

#import "RNNEventEmitter.h"
#import "RNNSplashScreen.h"
#import "RNNBridgeModule.h"
#import "RNNComponentViewCreator.h"
#import "RNNReactRootViewCreator.h"
#import "RNNReactComponentRegistry.h"

@interface RNNBridgeManager() <RCTBridgeDelegate>

@property (nonatomic, strong, readwrite) RCTBridge *bridge;
@property (nonatomic, strong, readwrite) RNNExternalComponentStore *store;
@property (nonatomic, strong, readwrite) RNNReactComponentRegistry *componentRegistry;
@property (nonatomic, strong, readonly) RNNOverlayManager *overlayManager;
@property (nonatomic, strong, readonly) RNNModalManager *modalManager;

@end

@implementation RNNBridgeManager {
	NSURL* _jsCodeLocation;
	NSDictionary* _launchOptions;
	id<RCTBridgeDelegate> _delegate;
	RCTBridge* _bridge;
	UIWindow* _mainWindow;
	
	RNNExternalComponentStore* _store;

	RNNCommandsHandler* _commandsHandler;
}

- (instancetype)initWithJsCodeLocation:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions bridgeManagerDelegate:(id<RCTBridgeDelegate>)delegate mainWindow:(UIWindow *)mainWindow {
	if (self = [super init]) {
		_mainWindow = mainWindow;
		_jsCodeLocation = jsCodeLocation;
		_launchOptions = launchOptions;
		_delegate = delegate;
		
		_overlayManager = [RNNOverlayManager new];
		
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

- (void)setJSCodeLocation:(NSURL *)jsCodeLocation {
	_jsCodeLocation = jsCodeLocation;
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
    if ([_delegate respondsToSelector:@selector(sourceURLForBridge:)]) {
        return [_delegate sourceURLForBridge:bridge];
    } else {
        return _jsCodeLocation;
    }
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
	RNNEventEmitter *eventEmitter = [[RNNEventEmitter alloc] init];
    _modalManager = [[RNNModalManager alloc] initWithBridge:bridge];
    
	id<RNNComponentViewCreator> rootViewCreator = [[RNNReactRootViewCreator alloc] initWithBridge:bridge eventEmitter:eventEmitter];
	_componentRegistry = [[RNNReactComponentRegistry alloc] initWithCreator:rootViewCreator];
	RNNControllerFactory *controllerFactory = [[RNNControllerFactory alloc] initWithRootViewCreator:rootViewCreator eventEmitter:eventEmitter store:_store componentRegistry:_componentRegistry andBridge:bridge bottomTabsAttachModeFactory:[BottomTabsAttachModeFactory new]];

	_commandsHandler = [[RNNCommandsHandler alloc] initWithControllerFactory:controllerFactory eventEmitter:eventEmitter modalManager:_modalManager overlayManager:_overlayManager mainWindow:_mainWindow];
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
	[_overlayManager dismissAllOverlays];
	[_modalManager dismissAllModalsSynchronosly];
	[_componentRegistry clear];
	UIApplication.sharedApplication.delegate.window.rootViewController = nil;
}

@end

