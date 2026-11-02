#import "RNNAppDelegate.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import <react/featureflags/ReactNativeFeatureFlags.h>
#import <react/featureflags/ReactNativeFeatureFlagsDefaults.h>

#import "RCTAppSetupUtils.h"
#if __has_include(<React/RCTCxxBridgeDelegate.h>)
#import <React/RCTCxxBridgeDelegate.h>
#endif
#import <React/RCTLegacyViewManagerInteropComponentView.h>
#import <React/RCTRootView.h>
#import <React/RCTSurfacePresenter.h>
#if __has_include(<React/RCTSurfacePresenterStub.h>)
#import <React/RCTSurfacePresenterStub.h>
#endif
#if __has_include(<React/RCTSurfacePresenterBridgeAdapter.h>)
#import <React/RCTSurfacePresenterBridgeAdapter.h>
#endif
#import <ReactCommon/RCTTurboModuleManager.h>

#if __has_include(<react/config/ReactNativeConfig.h>)
#import <react/config/ReactNativeConfig.h>
#endif

#import <react/renderer/runtimescheduler/RuntimeScheduler.h>
#import <react/renderer/runtimescheduler/RuntimeSchedulerCallInvoker.h>
#import <React/RCTSurfacePresenter.h>
#if __has_include(<React/RCTBridge+Private.h>)
#import <React/RCTBridge+Private.h>
#endif
#import <React/RCTImageLoader.h>
#if __has_include(<React/RCTBridgeProxy.h>)
#import <React/RCTBridgeProxy.h>
#endif
#import <React/RCTSurfacePresenter.h>
#import <react/utils/ManagedObjectWrapper.h>

#import <React/RCTComponentViewFactory.h>

#import "RNNDeepLinkDispatcher.h"
#import "RNNReactNativeVersion.h"

static NSString *const kRNConcurrentRoot = @"concurrentRoot";

#if !RNN_RN_VERSION_79_OR_NEWER
    @interface RNNAppDelegate () <RCTTurboModuleManagerDelegate,
    RCTComponentViewFactoryComponentProvider> {
    }
    @end
#else
    @interface RNNAppDelegate () {
    }
    @end
#endif

@implementation RNNAppDelegate

- (BOOL)application:(UIApplication *)application
didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
// RN 0.77 & RN0.78
#if !RNN_RN_VERSION_79_OR_NEWER
#if __has_include(<ReactAppDependencyProvider/RCTAppDependencyProvider.h>)
    self.dependencyProvider = [RCTAppDependencyProvider new];
#endif
    // RN 0.77
    #if !RNN_RN_VERSION_78
        [self _setUpFeatureFlags];
        self.rootViewFactory = [self createRCTRootViewFactory];
    #else // RN0.78
        self.reactNativeFactory = [[RCTReactNativeFactory alloc] initWithDelegate:self];
        self.reactNativeFactory.rootViewFactory = [self createRCTRootViewFactory];
    #endif
    
    [RCTComponentViewFactory currentComponentViewFactory].thirdPartyFabricComponentsProvider = self;
    RCTAppSetupPrepareApp(application, self.newArchEnabled);
    RCTSetNewArchEnabled(TRUE);
    
    RCTEnableTurboModuleInterop(YES);
    RCTEnableTurboModuleInteropBridgeProxy(YES);
    
    self.rootViewFactory.reactHost = [self.rootViewFactory createReactHost:launchOptions];
    
    [ReactNativeNavigation bootstrapWithHost:self.rootViewFactory.reactHost];
#else
    self.reactNativeFactory = [[RCTReactNativeFactory alloc] initWithDelegate:self.reactNativeDelegate];
    self.reactNativeDelegate.dependencyProvider = [RCTAppDependencyProvider new];
    
#if !RNN_RN_VERSION_88_OR_NEWER
    RCTAppSetupPrepareApp(application, YES);
    RCTEnableTurboModuleInterop(YES);
    RCTEnableTurboModuleInteropBridgeProxy(YES);
#else
    // RN 0.88 removed RCTAppSetupPrepareApp (TurboModules are always on) and the interop bridge
    // proxy. Keep the rest of what RCTAppSetupPrepareApp did.
#if DEBUG
    application.idleTimerDisabled = YES;
#endif
#ifndef RCT_REMOVE_LEGACY_MODULE_INTEROP
    RCTEnableTurboModuleInterop(YES);
#endif
#endif
    
    self.reactNativeFactory.rootViewFactory.reactHost = [self.reactNativeFactory.rootViewFactory createReactHost:launchOptions];
    
    [ReactNativeNavigation bootstrapWithHost:self.reactNativeFactory.rootViewFactory.reactHost];
#endif
    
    // Created now so it observes the first `RCTContentDidAppearNotification` and can flush links
    // that arrive before the React runtime is ready.
    [RNNDeepLinkDispatcher sharedDispatcher];
    
    return YES;
}

#pragma mark - Deep linking

// Forward OS-delivered custom-scheme URLs to React Native's Linking module.
- (BOOL)application:(UIApplication *)application
            openURL:(NSURL *)url
            options:(NSDictionary<UIApplicationOpenURLOptionsKey, id> *)options {
    [self dispatchDeepLinkURL:url];
    return YES;
}

// Forward universal links (associated domains) to React Native's Linking
// module by extracting the underlying https URL and routing it through the
// same pre-bridge queue as everything else.
- (BOOL)application:(UIApplication *)application
   continueUserActivity:(NSUserActivity *)userActivity
     restorationHandler:
         (void (^)(NSArray<id<UIUserActivityRestoring>> *_Nullable))restorationHandler {
    if ([userActivity.activityType isEqualToString:NSUserActivityTypeBrowsingWeb]) {
        [self dispatchDeepLinkURL:userActivity.webpageURL];
        return YES;
    }
    return NO;
}

- (void)dispatchDeepLinkURL:(NSURL *)url {
    [ReactNativeNavigation dispatchDeepLinkURL:url];
}


#if !RNN_RN_VERSION_79_OR_NEWER
- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
    [NSException raise:@"RCTBridgeDelegate::sourceURLForBridge not implemented"
                format:@"Subclasses must implement a valid sourceURLForBridge method"];
    return nil;
}

- (BOOL)concurrentRootEnabled {
    return true;
}

- (RCTRootViewFactory *)createRCTRootViewFactory
{
    __weak __typeof(self) weakSelf = self;
    RCTBundleURLBlock bundleUrlBlock = ^{
        RCTAppDelegate *strongSelf = weakSelf;
        return strongSelf.bundleURL;
    };
    
    RCTRootViewFactoryConfiguration *configuration =
    [[RCTRootViewFactoryConfiguration alloc] initWithBundleURLBlock:bundleUrlBlock
                                                     newArchEnabled:self.newArchEnabled];
    
    
    return [[RCTRootViewFactory alloc] initWithConfiguration:configuration andTurboModuleManagerDelegate:self];
}

#pragma mark - RCTTurboModuleManagerDelegate

- (id<RCTTurboModule>)getModuleInstanceFromClass:(Class)moduleClass
{
    return RCTAppSetupDefaultModuleFromClass(moduleClass, self.dependencyProvider);
}

#pragma mark - Feature Flags
class RCTAppDelegateBridgelessFeatureFlags : public facebook::react::ReactNativeFeatureFlagsDefaults {
public:
    bool enableBridgelessArchitecture() override
    {
        return true;
    }
    bool enableFabricRenderer() override
    {
        return true;
    }
    bool useTurboModules() override
    {
        return true;
    }
    bool useNativeViewConfigsInBridgelessMode() override
    {
        return true;
    }
    
    
    bool enableFixForViewCommandRace() override
    {
        return true;
    }
};

- (void)_setUpFeatureFlags
{
    facebook::react::ReactNativeFeatureFlags::override(
                                                       std::make_unique<RCTAppDelegateBridgelessFeatureFlags>());
}
#endif

@end
