#import <Foundation/Foundation.h>
#import <React/CoreModulesPlugins.h>
#import <ReactNativeNavigation/ReactNativeVersionExtracted.h>

#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
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

#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
@interface RNNAppDelegate : RCTAppDelegate
#else
@interface RNNAppDelegate : UIResponder <UIApplicationDelegate>
@property(nonatomic, strong) UIWindow *window;

@property(nonatomic, strong) RCTDefaultReactNativeFactoryDelegate *reactNativeDelegate;

@property(nonatomic, strong) RCTReactNativeFactory *reactNativeFactory;
@property(nonatomic) BOOL bridgelessEnabled;
#endif

@end
