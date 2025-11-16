#import "AppDelegate.h"
#import "RNNCustomViewController.h"
//#import <ReactAppDependencyProvider/RCTAppDependencyProvider.h>
#import <React/RCTBundleURLProvider.h>
#import <ReactNativeNavigation/ReactNativeNavigation.h>

#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
@interface AppDelegate () <RCTBridgeDelegate>
@end
#else
@interface AppDelegate ()
@end

#endif

#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR >= 79 || RN_VERSION_MAJOR > 0
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
	[super application:application didFinishLaunchingWithOptions:launchOptions];
	
	
#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
	self.dependencyProvider = [RCTAppDependencyProvider new];
#else
	//self.reactNativeDelegate = [ReactNativeDelegate new];
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
