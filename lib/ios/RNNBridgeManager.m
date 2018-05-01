#import "RNNBridgeManager.h"

#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

#import "RNNEventEmitter.h"
#import "RNNSplashScreen.h"
#import "RNNBridgeModule.h"
#import "RNNRootViewCreator.h"
#import "RNNReactRootViewCreator.h"

@interface RNNBridgeManager() <RCTBridgeDelegate>

@property (nonatomic, strong, readwrite) RCTBridge *bridge;

@end

@implementation RNNBridgeManager {
	NSURL* _jsCodeLocation;
	NSDictionary* _launchOptions;
	RCTBridge* _bridge;

	RNNStore* _store;

	RNNCommandsHandler* _commandsHandler;
}

- (instancetype)initWithJsCodeLocation:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions {
	if (self = [super init]) {
		_jsCodeLocation = jsCodeLocation;
		_launchOptions = launchOptions;
		
		_store = [RNNStore new];
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

# pragma mark - RCTBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
	return _jsCodeLocation;
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
	RNNEventEmitter *eventEmitter = [[RNNEventEmitter alloc] init];

	id<RNNRootViewCreator> rootViewCreator = [[RNNReactRootViewCreator alloc] initWithBridge:bridge];
	RNNControllerFactory *controllerFactory = [[RNNControllerFactory alloc] initWithRootViewCreator:rootViewCreator store:_store eventEmitter:eventEmitter andBridge:bridge];
	_commandsHandler = [[RNNCommandsHandler alloc] initWithStore:_store controllerFactory:controllerFactory eventEmitter:eventEmitter];
	RNNBridgeModule *bridgeModule = [[RNNBridgeModule alloc] initWithCommandsHandler:_commandsHandler];

	return @[bridgeModule,eventEmitter];
}

# pragma mark - JavaScript & Bridge Notifications

- (void)onJavaScriptWillLoad {
	[_store clean];
}

- (void)onJavaScriptLoaded {
	[_store setReadyToReceiveCommands:true];
	[[_bridge moduleForClass:[RNNEventEmitter class]] sendOnAppLaunched];
}

- (void)onBridgeWillReload {
	UIApplication.sharedApplication.delegate.window.rootViewController =  nil;
}

@end

