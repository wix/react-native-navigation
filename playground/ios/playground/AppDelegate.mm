#import "AppDelegate.h"
#import "RNNCustomViewController.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import <UserNotifications/UserNotifications.h>

#if !RNN_RN_VERSION_79_OR_NEWER
@interface AppDelegate () <RCTBridgeDelegate, UNUserNotificationCenterDelegate>
@end
#else
@interface AppDelegate () <UNUserNotificationCenterDelegate>
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

	// Demo: route notification taps through RNN's deep-linking pipeline by
	// installing this AppDelegate as the UNUserNotificationCenter delegate.
	// (Apps that already own this delegate via Firebase/OneSignal/etc. can
	// instead call `[self dispatchDeepLinkURL:url]` from their existing
	// handler — same effect.)
	[UNUserNotificationCenter currentNotificationCenter].delegate = self;

	return YES;
}

#pragma mark - UNUserNotificationCenterDelegate

// Surface notifications while the app is in the foreground so the user
// (and Detox) can see them before the tap is simulated.
//
// NOTE: `UNNotificationPresentationOptionAlert` is intentionally included
// alongside the iOS 14+ `.banner`/`.list` flags because Detox checks
// `options.contains(.alert)` in DetoxUserNotificationDispatcher before
// invoking the tap path. Dropping `.alert` would break e2e notification
// tests on Detox even though iOS 14+ otherwise prefers banner/list.
- (void)userNotificationCenter:(UNUserNotificationCenter *)center
       willPresentNotification:(UNNotification *)notification
         withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
	completionHandler(UNNotificationPresentationOptionAlert |
                      UNNotificationPresentationOptionBanner |
                      UNNotificationPresentationOptionList |
                      UNNotificationPresentationOptionSound);
}

// Notification tap → if payload carries `url`, route it through RNN's
// deep-linking pipeline (inherited `dispatchDeepLinkURL:` queues until
// the React runtime is ready, then forwards to RCTLinkingManager).
- (void)userNotificationCenter:(UNUserNotificationCenter *)center
didReceiveNotificationResponse:(UNNotificationResponse *)response
         withCompletionHandler:(void (^)(void))completionHandler {
	NSDictionary *userInfo = response.notification.request.content.userInfo;
	NSString *urlString = userInfo[@"url"];
	if ([urlString isKindOfClass:[NSString class]]) {
		NSURL *url = [NSURL URLWithString:urlString];
		[self dispatchDeepLinkURL:url];
	}
	completionHandler();
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
