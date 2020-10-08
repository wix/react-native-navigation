#import "AppDelegate.h"

#import <React/RCTBridge.h>
#import <React/RCTBundleURLProvider.h>

#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import "RNNCustomViewController.h"

@interface AppDelegate() <RCTBridgeDelegate>
@end

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
	self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
	if (@available(iOS 13.0, *)) {
		self.window.backgroundColor = [UIColor systemBackgroundColor];
	} else {
		self.window.backgroundColor = [UIColor whiteColor];
	}
	[self.window makeKeyWindow];

	[ReactNativeNavigation bootstrapWithDelegate:self launchOptions:launchOptions];
	[ReactNativeNavigation registerExternalComponent:@"RNNCustomComponent" callback:^UIViewController *(NSDictionary *props, RCTBridge *bridge) {
		return [[RNNCustomViewController alloc] initWithProps:props];
	}];
	
	return YES;
}

# pragma mark - RCTBridgeDelegate

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge
{
#if DEBUG
  return [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index" fallbackResource:nil];
#else
  return [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];
#endif
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
	return [ReactNativeNavigation extraModulesForBridge:bridge];
}

@end
