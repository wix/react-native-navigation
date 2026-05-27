#import "RNNAppDelegate.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import <react/featureflags/ReactNativeFeatureFlags.h>
#import <react/featureflags/ReactNativeFeatureFlagsDefaults.h>

#import "RCTAppSetupUtils.h"
#if __has_include(<React/RCTCxxBridgeDelegate.h>)
#import <React/RCTCxxBridgeDelegate.h>
#endif
#import <React/RCTLegacyViewManagerInteropComponentView.h>
#import <React/RCTLinkingManager.h>
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

// Deep-link URLs that arrive (openURL, universal link, or external dispatch)
// before the React runtime is ready are queued here and flushed when Fabric
// posts `RCTContentDidAppearNotification` — by which point
// `RCTLinkingManager` is instantiated and JS subscribers are listening.
static NSMutableArray<NSURL *> *gRNNPendingDeepLinkURLs = nil;
static BOOL gRNNReactRuntimeReady = NO;


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
    
    RCTAppSetupPrepareApp(application, YES);
    RCTEnableTurboModuleInterop(YES);
    RCTEnableTurboModuleInteropBridgeProxy(YES);
    
    self.reactNativeFactory.rootViewFactory.reactHost = [self.reactNativeFactory.rootViewFactory createReactHost:launchOptions];
    
    [ReactNativeNavigation bootstrapWithHost:self.reactNativeFactory.rootViewFactory.reactHost];
#endif
    
    [self rnn_installDeepLinkObservers];
    
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
    if (url == nil) {
        return;
    }
    if (gRNNReactRuntimeReady) {
        [RCTLinkingManager application:[UIApplication sharedApplication]
                               openURL:url
                               options:@{}];
        return;
    }
    if (gRNNPendingDeepLinkURLs == nil) {
        gRNNPendingDeepLinkURLs = [NSMutableArray array];
    }
    [gRNNPendingDeepLinkURLs addObject:url];
}

- (void)rnn_installDeepLinkObservers {
    // `RCTContentDidAppearNotification` is posted by Fabric's root view
    // once content has rendered. RNN forces bridgeless/new-arch, so the
    // legacy `RCTJavaScriptDidLoadNotification` never fires; we rely on
    // this Fabric signal exclusively.
    [[NSNotificationCenter defaultCenter] addObserver:self
                                             selector:@selector(rnn_handleReactRuntimeReady:)
                                                 name:RCTContentDidAppearNotification
                                               object:nil];
}

- (void)rnn_handleReactRuntimeReady:(NSNotification *)notification {
    gRNNReactRuntimeReady = YES;
    NSArray<NSURL *> *pending = [gRNNPendingDeepLinkURLs copy];
    [gRNNPendingDeepLinkURLs removeAllObjects];
    for (NSURL *url in pending) {
        [RCTLinkingManager application:[UIApplication sharedApplication]
                               openURL:url
                               options:@{}];
    }
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
