#import "AppDelegate.h"
#import "RNNCustomViewController.h"
#import <React/RCTBundleURLProvider.h>
#import <ReactNativeNavigation/ReactNativeNavigation.h>

@interface AppDelegate () <RCTBridgeDelegate>
@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    [super application:application didFinishLaunchingWithOptions:launchOptions];
	
	if (self.bridgelessEnabled) {
#ifdef RCT_NEW_ARCH_ENABLED
		[ReactNativeNavigation
			registerExternalHostComponent: @"RNNCustomComponent"
							 callback:^UIViewController *(NSDictionary *props, RCTHost *host) {
							   return [[RNNCustomViewController alloc] initWithProps:props];
							 }];
#endif
	} else {
		[ReactNativeNavigation
			registerExternalComponent:@"RNNCustomComponent"
							 callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) {
							   return [[RNNCustomViewController alloc] initWithProps:props];
							 }];
	}
	
    return YES;
}

#pragma mark - RCTBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
  return [self bundleURL];
}

- (NSURL *)bundleURL
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

@end
