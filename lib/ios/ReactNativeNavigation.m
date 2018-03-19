#import "ReactNativeNavigation.h"

#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

#import "RNNBridgeManager.h"
#import "RNNSplashScreen.h"

@interface ReactNativeNavigation()

@property (nonatomic, strong) RNNBridgeManager *bridgeManager;

@end

@implementation ReactNativeNavigation

# pragma mark - public API

+(void)bootstrap:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions {
	[[ReactNativeNavigation sharedInstance] bootstrap:jsCodeLocation launchOptions:launchOptions];
}

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
	[[ReactNativeNavigation sharedInstance].bridgeManager registerExternalComponent:name callback:callback];
}

# pragma mark - instance

+ (instancetype) sharedInstance {
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
	self.bridgeManager = [[RNNBridgeManager alloc] initWithJsCodeLocation:jsCodeLocation launchOptions:launchOptions];
	[RNNSplashScreen show];
}

@end
