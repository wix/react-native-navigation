#import "AppDelegate.h"
#import "RNNCustomViewController.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>

#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
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
#if (RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR >= 79) || RN_VERSION_MAJOR > 0
	self.reactNativeDelegate = [ReactNativeDelegate new];
#endif
	
	[super application:application didFinishLaunchingWithOptions:launchOptions];
	
	
#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
	self.dependencyProvider = [RCTAppDependencyProvider new];
#endif
	
	[ReactNativeNavigation
	 registerExternalHostComponent: @"RNNCustomComponent"
	 callback:^UIViewController *(NSDictionary *props, RCTHost *host) {
		return [[RNNCustomViewController alloc] initWithProps:props];
	}];
	
	return YES;
}

#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
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

