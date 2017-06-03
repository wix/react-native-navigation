
#import "ReactNativeNavigation.h"

#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

#import "RNNEventEmitter.h"
#import "RNNSplashScreen.h"
#import "RNNBridgeModule.h"
#import "RNNRootViewCreator.h"
#import "RNNReactRootViewCreator.h"

@interface ReactNativeNavigation() <RCTBridgeDelegate>

@end

@implementation ReactNativeNavigation {
	NSURL* _jsCodeLocation;
	NSDictionary* _launchOptions;
	RCTBridge* _bridge;
	
	RNNStore* _store;
	
	RNNCommandsHandler* _commandsHandler;
}

# pragma mark - public API

+(void)bootstrap:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions {
	[[ReactNativeNavigation sharedInstance] bootstrap:jsCodeLocation launchOptions:launchOptions];
}

# pragma mark - instance

+(instancetype) sharedInstance {
	static ReactNativeNavigation *instance = nil;
	static dispatch_once_t onceToken = 0;
	dispatch_once(&onceToken,^{
		if (instance == nil) {
			instance = [[ReactNativeNavigation alloc] init];
		}
	});
	
	return instance;
}

-(void)bootstrap:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions {
	_jsCodeLocation = jsCodeLocation;
	_launchOptions = launchOptions;
	_store = [RNNStore new];
	
	[RNNSplashScreen show];
	
	[self registerForJsEvents];
	
	[self createBridgeLoadJsAndThenInitDependencyGraph];
}

# pragma mark - RCTBridgeDelegate

-(NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
	return _jsCodeLocation;
}

/**
 * here we initialize all of our dependency graph
 */
-(NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
	RNNEventEmitter *eventEmitter = [[RNNEventEmitter alloc] init];
	
	id<RNNRootViewCreator> rootViewCreator = [[RNNReactRootViewCreator alloc] initWithBridge:bridge];
	RNNControllerFactory *controllerFactory = [[RNNControllerFactory alloc] initWithRootViewCreator:rootViewCreator store:_store eventEmitter:eventEmitter];
	_commandsHandler = [[RNNCommandsHandler alloc] initWithStore:_store controllerFactory:controllerFactory];
	RNNBridgeModule *bridgeModule = [[RNNBridgeModule alloc] initWithCommandsHandler:_commandsHandler];
	
	return @[bridgeModule,eventEmitter];
}

# pragma mark - js events

-(void)onJavaScriptWillLoad {
	[_store clean];
}

-(void)onJavaScriptLoaded {
	[_store setReadyToReceiveCommands:true];
	[[_bridge moduleForClass:[RNNEventEmitter class]] sendOnAppLaunched];
}

# pragma mark - private

-(void)createBridgeLoadJsAndThenInitDependencyGraph {
	_bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:_launchOptions];
}

-(void)registerForJsEvents {
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJavaScriptLoaded)
												 name:RCTJavaScriptDidLoadNotification
											   object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self
											 selector:@selector(onJavaScriptWillLoad)
												 name:RCTJavaScriptWillStartLoadingNotification
											   object:nil];
}

@end
