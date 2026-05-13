#import <Foundation/Foundation.h>
#import <React/CoreModulesPlugins.h>

// Detect RN 0.79+ by checking for RCTJSRuntimeConfiguratorProtocol.h (added in 0.79)
#if __has_include(<React-RCTAppDelegate/RCTJSRuntimeConfiguratorProtocol.h>) || \
    __has_include(<React_RCTAppDelegate/RCTJSRuntimeConfiguratorProtocol.h>)
#define RNN_RN_VERSION_79_OR_NEWER 1
#else
#define RNN_RN_VERSION_79_OR_NEWER 0
// Detect RN 0.78 by checking for RCTReactNativeFactory.h (added in 0.78, doesn't exist in 0.77)
#if __has_include(<React-RCTAppDelegate/RCTReactNativeFactory.h>) || \
        __has_include(<React_RCTAppDelegate/RCTReactNativeFactory.h>)
#define RNN_RN_VERSION_78 1
#else
#define RNN_RN_VERSION_78 0
#endif
#endif

#if !RNN_RN_VERSION_79_OR_NEWER
#if __has_include(<React-RCTAppDelegate/RCTAppDelegate.h>)
#import <React-RCTAppDelegate/RCTAppDelegate.h>
#elif __has_include(<React_RCTAppDelegate/RCTAppDelegate.h>)
#import <React_RCTAppDelegate/RCTAppDelegate.h>
#endif

#import <React/RCTBridge.h>
#else
#if __has_include(<React-RCTAppDelegate/RCTDefaultReactNativeFactoryDelegate.h>)
#import <React-RCTAppDelegate/RCTDefaultReactNativeFactoryDelegate.h>
#elif __has_include(<React_RCTAppDelegate/RCTDefaultReactNativeFactoryDelegate.h>)
#import <React_RCTAppDelegate/RCTDefaultReactNativeFactoryDelegate.h>
#endif
#endif

#if __has_include(<ReactAppDependencyProvider/RCTAppDependencyProvider.h>)
#import <ReactAppDependencyProvider/RCTAppDependencyProvider.h>
#endif

#if __has_include(<React-RCTAppDelegate/RCTReactNativeFactory.h>)
#import <React-RCTAppDelegate/RCTReactNativeFactory.h>
#elif __has_include(<React_RCTAppDelegate/RCTReactNativeFactory.h>)
#import <React_RCTAppDelegate/RCTReactNativeFactory.h>
#endif

#import <React/RCTBundleURLProvider.h>

#if !RNN_RN_VERSION_79_OR_NEWER
@interface RNNAppDelegate : RCTAppDelegate
#else
@interface RNNAppDelegate : UIResponder <UIApplicationDelegate>
@property(nonatomic, strong) UIWindow *window;

@property(nonatomic, strong) RCTDefaultReactNativeFactoryDelegate *reactNativeDelegate;

@property(nonatomic, strong) RCTReactNativeFactory *reactNativeFactory;
@property(nonatomic) BOOL bridgelessEnabled;
#endif

/**
 * Dispatch a deep link URL through React Native's Linking module so JS
 * subscribers (including RNN's built-in deep linking framework) receive it.
 *
 * Safe to call before the JS bridge is ready: URLs that arrive early
 * (e.g. cold-start notification taps) are queued natively and flushed
 * automatically once Fabric/React content first appears.
 *
 * Custom-scheme and universal-link openings dispatched by the OS are
 * forwarded through this method automatically; call it manually only
 * when your app receives a deep link from a source RNN can't intercept
 * (e.g. a custom `UNUserNotificationCenterDelegate`, a third-party push
 * SDK callback, etc.).
 */
- (void)dispatchDeepLinkURL:(NSURL *)url;

@end
