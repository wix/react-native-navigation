#import "RNNAppDelegate.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>

#if RCT_NEW_ARCH_ENABLED
#import "RCTAppSetupUtils.h"
#import "RCTLegacyInteropComponents.h"
#import <React/CoreModulesPlugins.h>
#import <React/RCTCxxBridgeDelegate.h>
#import <React/RCTFabricSurfaceHostingProxyRootView.h>
#import <React/RCTLegacyViewManagerInteropComponentView.h>
#import <React/RCTRuntimeExecutorFromBridge.h>
#import <React/RCTSurfacePresenter.h>
#import <React/RCTSurfacePresenterBridgeAdapter.h>
#import <ReactCommon/RCTTurboModuleManager.h>
#import <react/config/ReactNativeConfig.h>
#import <react/renderer/runtimescheduler/RuntimeScheduler.h>
#import <react/renderer/runtimescheduler/RuntimeSchedulerCallInvoker.h>

static NSString *const kRNConcurrentRoot = @"concurrentRoot";

@interface RNNAppDelegate () <RCTTurboModuleManagerDelegate, RCTCxxBridgeDelegate> {
    std::shared_ptr<const facebook::react::ReactNativeConfig> _reactNativeConfig;
    facebook::react::ContextContainer::Shared _contextContainer;
    std::shared_ptr<facebook::react::RuntimeScheduler> _runtimeScheduler;
}
@end

#endif

@implementation RNNAppDelegate

#if RCT_NEW_ARCH_ENABLED
- (instancetype)init {
    if (self = [super init]) {
        _contextContainer = std::make_shared<facebook::react::ContextContainer const>();
        _reactNativeConfig = std::make_shared<facebook::react::EmptyReactNativeConfig const>();
        _contextContainer->insert("ReactNativeConfig", _reactNativeConfig);
    }
    return self;
}
#endif

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
#if RCT_NEW_ARCH_ENABLED
    RCTEnableTurboModule(true);
#endif
    RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];
#if RCT_NEW_ARCH_ENABLED
    self.bridgeAdapter =
        [[RCTSurfacePresenterBridgeAdapter alloc] initWithBridge:bridge
                                                contextContainer:_contextContainer];
    bridge.surfacePresenter = self.bridgeAdapter.surfacePresenter;

    [self unstable_registerLegacyComponents];
#endif

    [ReactNativeNavigation bootstrapWithBridge:bridge];

    return YES;
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    return [ReactNativeNavigation extraModulesForBridge:bridge];
}

#if RCT_NEW_ARCH_ENABLED
#pragma mark - RCTCxxBridgeDelegate
- (std::unique_ptr<facebook::react::JSExecutorFactory>)jsExecutorFactoryForBridge:
    (RCTBridge *)bridge {
    _runtimeScheduler = _runtimeScheduler =
        std::make_shared<facebook::react::RuntimeScheduler>(RCTRuntimeExecutorFromBridge(bridge));
    std::shared_ptr<facebook::react::CallInvoker> callInvoker =
        std::make_shared<facebook::react::RuntimeSchedulerCallInvoker>(_runtimeScheduler);
    self.turboModuleManager = [[RCTTurboModuleManager alloc] initWithBridge:bridge
                                                                   delegate:self
                                                                  jsInvoker:callInvoker];
    _contextContainer->erase("RuntimeScheduler");
    _contextContainer->insert("RuntimeScheduler", _runtimeScheduler);
    return RCTAppSetupDefaultJsExecutorFactory(bridge, _turboModuleManager, _runtimeScheduler);
}

#pragma mark RCTTurboModuleManagerDelegate

- (Class)getModuleClassFromName:(const char *)name {
    return RCTCoreModulesClassProvider(name);
}

- (std::shared_ptr<facebook::react::TurboModule>)
    getTurboModule:(const std::string &)name
         jsInvoker:(std::shared_ptr<facebook::react::CallInvoker>)jsInvoker {
    return nullptr;
}

- (std::shared_ptr<facebook::react::TurboModule>)
    getTurboModule:(const std::string &)name
        initParams:(const facebook::react::ObjCTurboModule::InitParams &)params {
    return nullptr;
}

- (id<RCTTurboModule>)getModuleInstanceFromClass:(Class)moduleClass {
    return RCTAppSetupDefaultModuleFromClass(moduleClass);
}

#pragma mark - New Arch Enabled settings

- (BOOL)turboModuleEnabled {
    return YES;
}

- (BOOL)fabricEnabled {
    return YES;
}

#pragma mark - New Arch Utilities

- (void)unstable_registerLegacyComponents {
    for (NSString *legacyComponent in [RCTLegacyInteropComponents legacyInteropComponents]) {
        [RCTLegacyViewManagerInteropComponentView supportLegacyViewManagerWithName:legacyComponent];
    }
}

#endif

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
    [NSException raise:@"RCTBridgeDelegate::sourceURLForBridge not implemented"
                format:@"Subclasses must implement a valid sourceURLForBridge method"];
    return nil;
}

- (BOOL)concurrentRootEnabled {
    return true;
}

@end
