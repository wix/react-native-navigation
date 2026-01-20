#import "AppDelegate.h"
#import "RNNCustomViewController.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>

#if !RNN_RN_VERSION_79_OR_NEWER
@interface AppDelegate () <RCTBridgeDelegate>
@end
#else
@interface AppDelegate ()
@end

@interface ReactNativeDelegate : RCTDefaultReactNativeFactoryDelegate
@end

@implementation ReactNativeDelegate
- (NSURL *)bundleURL
{
#if DEBUG
	return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index"];
#else
	return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

- (BOOL)newArchEnabled
{
	return YES;
}

@end
#endif


@implementation AppDelegate

- (BOOL)application:(UIApplication *)application
didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
#if RNN_RN_VERSION_79_OR_NEWER
	self.reactNativeDelegate = [ReactNativeDelegate new];
#endif
	
	[super application:application didFinishLaunchingWithOptions:launchOptions];
	
	
#if !RNN_RN_VERSION_79_OR_NEWER
	self.dependencyProvider = [RCTAppDependencyProvider new];
#endif
	
	[ReactNativeNavigation
	 registerExternalHostComponent: @"RNNCustomComponent"
	 callback:^UIViewController *(NSDictionary *props, RCTHost *host) {
		return [[RNNCustomViewController alloc] initWithProps:props];
	}];
	
	return YES;
}

#if !RNN_RN_VERSION_79_OR_NEWER
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
#endif

@end

