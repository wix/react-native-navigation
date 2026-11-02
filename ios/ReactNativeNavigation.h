#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <UIKit/UIKit.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <React-RuntimeApple/ReactCommon/RCTHost.h>
#import "RNNTurboManager.h"
#endif

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary *props, RCTBridge *bridge);

@interface ReactNativeNavigation : NSObject

#ifdef RCT_NEW_ARCH_ENABLED
+ (void)bootstrapWithHost:(RCTHost *)host;
+ (void)registerExternalHostComponent:(NSString *)name callback:(RNNExternalHostViewCreator)callback;
+ (RCTHost *)getHost;
#endif

+ (void)bootstrapWithBridge:(RCTBridge *)bridge;

+ (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge;

+ (RCTBridge *)getBridge;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

+ (UIViewController *)findViewController:(NSString *)componentId;

#pragma mark - Window

/**
 * The window RNN renders into. This is the only window RNN treats as its main window.
 *
 * Returns the app delegate's `window` when it has one. Otherwise it creates a full-screen window
 * and assigns it to the app delegate's `window`, so other libraries that read `delegate.window`
 * keep seeing the same object under both life cycles.
 */
+ (UIWindow *)mainWindow;

/**
 * Attaches the main window to `scene` and makes it key and visible. Call it from
 * `scene:willConnectToSession:options:` when the app uses the UIScene life cycle
 * (`RNNSceneDelegate` does this for you).
 *
 * If the main window already exists (created in `application:didFinishLaunchingWithOptions:`,
 * before the scene connected), it is moved to `scene`; otherwise it is created with
 * `initWithWindowScene:`. Windows RNN creates later (overlays, bottom-tab preloading) use the same
 * scene. Single scene only.
 */
+ (void)attachMainWindowToScene:(UIWindowScene *)scene;

#pragma mark - Deep linking

/**
 * Dispatches a URL to React Native's `Linking` module, queueing it until the React runtime is
 * ready. `-[RNNAppDelegate dispatchDeepLinkURL:]` forwards here.
 *
 * On React Native >= 0.88, in an app that uses the UIScene life cycle, React Native ignores URLs
 * that don't come with a `UIOpenURLContext`; such URLs are logged and dropped. Under scenes, pass
 * what iOS delivered with `dispatchOpenURLContexts:inScene:` / `dispatchUserActivity:inScene:`.
 */
+ (void)dispatchDeepLinkURL:(NSURL *)url;

/**
 * Dispatches the URL contexts iOS passed to `scene:openURLContexts:` (or the scene's connection
 * options) to `Linking`, queueing them until the React runtime is ready.
 */
+ (void)dispatchOpenURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts inScene:(UIScene *)scene;

/**
 * Dispatches a browsing-web user activity (universal link) iOS passed to
 * `scene:continueUserActivity:` (or the scene's connection options) to `Linking`, queueing it
 * until the React runtime is ready. Other activity types are ignored.
 */
+ (void)dispatchUserActivity:(NSUserActivity *)userActivity inScene:(UIScene *)scene;

@end
