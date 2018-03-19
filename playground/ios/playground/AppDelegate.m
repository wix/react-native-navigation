#import "AppDelegate.h"
#import "RNNCustomViewController.h"

// **********************************************
// *** DON'T MISS: THE NEXT LINE IS IMPORTANT ***
// **********************************************
#import <React/RCTBundleURLProvider.h>
#import <ReactNativeNavigation/ReactNativeNavigation.h>

// IMPORTANT: if you're getting an Xcode error that RCCManager.h isn't found, you've probably ran "npm install"
// with npm ver 2. You'll need to "npm install" with npm 3 (see https://github.com/wix/react-native-navigation/issues/1)

#import <React/RCTRootView.h>

@implementation AppDelegate

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
{
	// **********************************************
	// *** DON'T MISS: THIS IS HOW WE BOOTSTRAP *****
	// **********************************************
	NSURL *jsCodeLocation = [[RCTBundleURLProvider sharedSettings] jsBundleURLForBundleRoot:@"index.ios" fallbackResource:nil];
	[ReactNativeNavigation bootstrap:jsCodeLocation launchOptions:launchOptions];
	
	[ReactNativeNavigation registerExternalComponent:@"RNNCustomComponent" callback:^UIViewController *{
		return [[RNNCustomViewController alloc] init];
	}];
	
	/*
	// original RN bootstrap - remove this part
	RCTRootView *rootView = [[RCTRootView alloc] initWithBundleURL:jsCodeLocation
														moduleName:@"com.example.WelcomeScreen"
												 initialProperties:nil
													 launchOptions:launchOptions];
	self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
	UIViewController *rootViewController = [UIViewController new];
	rootViewController.view = rootView;
	self.window.rootViewController = rootViewController;
	[self.window makeKeyAndVisible];
	*/
	
	
	return YES;
}

@end
