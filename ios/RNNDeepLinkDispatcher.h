#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

/**
 * Forwards deep links to React Native's `Linking` module.
 *
 * Links that arrive before the React runtime is ready (cold-start URL launches, notification taps)
 * are queued and flushed once Fabric posts `RCTContentDidAppearNotification`; by then
 * `RCTLinkingManager` is instantiated and JS subscribers are listening.
 *
 * Each queued entry keeps what iOS delivered (an `NSURL`, a set of `UIOpenURLContext`s or an
 * `NSUserActivity`) and is replayed through the matching `RCTLinkingManager` entry point:
 * - RN >= 0.88 in a scene app (`RCTIsSceneDelegateApp()`): `+scene:openURLContexts:` and
 *   `+scene:continueUserActivity:`;
 * - otherwise `+application:openURL:options:`.
 *
 * A bare `NSURL` has no `UIOpenURLContext`, so on RN >= 0.88 in a scene app React Native drops it.
 * The dispatcher logs a warning when that happens.
 *
 * Use it through the class methods on `ReactNativeNavigation`.
 */
@interface RNNDeepLinkDispatcher : NSObject

+ (instancetype)sharedDispatcher;

- (void)dispatchURL:(nullable NSURL *)url;

- (void)dispatchOpenURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts scene:(UIScene *)scene;

- (void)dispatchUserActivity:(NSUserActivity *)userActivity scene:(UIScene *)scene;

@end

NS_ASSUME_NONNULL_END
