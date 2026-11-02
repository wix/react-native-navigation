#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

/**
 * Base class for the scene delegate of an app that adopts the UIScene life cycle. It is the scene
 * counterpart of `RNNAppDelegate`, which keeps creating the React host in
 * `application:didFinishLaunchingWithOptions:`.
 *
 * List the subclass as `UISceneDelegateClassName` in the app's `UIApplicationSceneManifest`.
 * Overrides of the methods below must call `super`.
 *
 * - `scene:willConnectToSession:options:` attaches RNN's main window to the scene
 *   (`+[ReactNativeNavigation attachMainWindowToScene:]`), records the launch links from the
 *   connection options and passes them to `dispatchLaunchLinksFromConnectionOptions:scene:`.
 * - `scene:openURLContexts:` and `scene:continueUserActivity:` pass what iOS delivered to RNN's
 *   deep-link queue, which forwards it to React Native's `Linking` once the runtime is ready.
 *
 * Single scene only (`UIApplicationSupportsMultipleScenes` = NO).
 */
@interface RNNSceneDelegate : UIResponder <UIWindowSceneDelegate>

/// RNN's main window, set once the scene connects.
@property(nonatomic, strong, nullable) UIWindow *window;

/// The URL contexts the scene was launched with (`connectionOptions.URLContexts`).
@property(nonatomic, copy, readonly) NSSet<UIOpenURLContext *> *launchURLContexts;

/// The user activities the scene was launched with (`connectionOptions.userActivities`).
@property(nonatomic, copy, readonly) NSSet<NSUserActivity *> *launchUserActivities;

/// The launch URL: the URL of a launch URL context (iOS normally passes one), otherwise the
/// `webpageURL` of a browsing-web launch user activity. Nil when the scene was not launched by a
/// link.
@property(nonatomic, readonly, nullable) NSURL *launchURL;

/**
 * Cold-start parity with the app-delegate life cycle. There, iOS calls `application:openURL:`
 * after launch and RNN queues the URL until React is ready. Under scenes, iOS delivers the launch
 * link only in the connection options and does not call `scene:openURLContexts:`.
 *
 * The default implementation passes the connection options' URL contexts and browsing-web user
 * activities to RNN's deep-link queue, so JS receives them as `Linking` `url` events, the same way
 * as on the app-delegate path. Override it without calling `super` if your app delivers the
 * initial link itself (for example through `launchURL`).
 */
- (void)dispatchLaunchLinksFromConnectionOptions:(UISceneConnectionOptions *)connectionOptions
                                           scene:(UIScene *)scene;

/// Same as `-[RNNAppDelegate dispatchDeepLinkURL:]`, including its limits under the UIScene life
/// cycle on React Native >= 0.88.
- (void)dispatchDeepLinkURL:(NSURL *)url;

@end

NS_ASSUME_NONNULL_END
