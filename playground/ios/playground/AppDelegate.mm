#import "AppDelegate.h"
#import <React/RCTBundleURLProvider.h>
#import "RNNCustomViewController.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>

@interface AppDelegate () <RCTBridgeDelegate>
@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
	[ReactNativeNavigation
		registerExternalComponent:@"RNNCustomComponent"
						 callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) {
						   return [[RNNCustomViewController alloc] initWithProps:props];
						 }];
	return [super application:application didFinishLaunchingWithOptions:launchOptions];
}

#pragma mark - RCTBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
#if DEBUG
    return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
    return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    return [ReactNativeNavigation extraModulesForBridge:bridge];
}


@end
