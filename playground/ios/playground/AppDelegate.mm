#import "AppDelegate.h"
#import "RNNCustomViewController.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import <React/RCTBridge.h>
#import <React/RCTLinkingManager.h>
#import <React/RCTRootView.h>
#import <UserNotifications/UserNotifications.h>

// URLs that arrive (notification tap, openURL) before the JS bridge is
// ready are queued here and flushed when the bridge finishes loading.
// Without this, cold-start notifications would post `RCTOpenURLNotification`
// into the void because RCTLinkingManager hasn't subscribed yet.
static NSMutableArray<NSURL *> *gPendingDeepLinkURLs = nil;
static BOOL gJavaScriptDidLoad = NO;

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

	// Receive notification taps (Detox sendUserNotification & real push taps)
	[UNUserNotificationCenter currentNotificationCenter].delegate = self;

	// Flush deep links that arrived before the JS bridge was up.
	// In legacy mode `RCTJavaScriptDidLoadNotification` fires after the
	// bridge loads JS. In bridgeless/new-arch that notification does NOT
	// fire, so we also listen for `RCTContentDidAppearNotification` which
	// is posted by Fabric's root view once content has rendered — by which
	// point RCTLinkingManager is already instantiated and listening.
	[[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleJavaScriptDidLoad:)
                                                 name:RCTJavaScriptDidLoadNotification
                                               object:nil];
	[[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(handleJavaScriptDidLoad:)
                                                 name:RCTContentDidAppearNotification
                                               object:nil];

	return YES;
}

#pragma mark - Deep linking

// Forward foreground URL openings (custom schemes & universal links)
// to React Native's Linking module so JS can handle them.
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
            options:(NSDictionary<UIApplicationOpenURLOptionsKey, id> *)options {
	[self dispatchDeepLinkURL:url];
	return YES;
}

// Dispatch a deep link URL. If RCTLinkingManager hasn't subscribed yet
// (cold-start, JS bridge still loading), queue it for replay after
// RCTJavaScriptDidLoadNotification fires.
- (void)dispatchDeepLinkURL:(NSURL *)url {
	if (url == nil) { return; }
	if (gJavaScriptDidLoad) {
		[RCTLinkingManager application:[UIApplication sharedApplication]
                               openURL:url
                               options:@{}];
		return;
	}
	if (gPendingDeepLinkURLs == nil) {
		gPendingDeepLinkURLs = [NSMutableArray array];
	}
	[gPendingDeepLinkURLs addObject:url];
}

- (void)handleJavaScriptDidLoad:(NSNotification *)notification {
	gJavaScriptDidLoad = YES;
	NSArray<NSURL *> *pending = [gPendingDeepLinkURLs copy];
	[gPendingDeepLinkURLs removeAllObjects];
	for (NSURL *url in pending) {
		[RCTLinkingManager application:[UIApplication sharedApplication]
                               openURL:url
                               options:@{}];
	}
}

- (BOOL)application:(UIApplication *)application
continueUserActivity:(NSUserActivity *)userActivity
 restorationHandler:(void (^)(NSArray<id<UIUserActivityRestoring>> *))restorationHandler {
	return [RCTLinkingManager application:application
                    continueUserActivity:userActivity
                      restorationHandler:restorationHandler];
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

// Notification tap → if payload carries `url`, route it through the
// existing Linking pipeline so deep linking reacts the same way regardless
// of whether the URL came from the OS or a notification.
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

