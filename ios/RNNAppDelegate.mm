#import "RNNAppDelegate.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>
#import <react/featureflags/ReactNativeFeatureFlags.h>
#import <react/featureflags/ReactNativeFeatureFlagsDefaults.h>

#import "RCTAppSetupUtils.h"
#import <React/CoreModulesPlugins.h>
#import <React/RCTCxxBridgeDelegate.h>
#import <React/RCTLegacyViewManagerInteropComponentView.h>
#import <React/RCTSurfacePresenter.h>
#import <React/RCTSurfacePresenterStub.h>
#import <React/RCTSurfacePresenterBridgeAdapter.h>
#import <ReactCommon/RCTTurboModuleManager.h>

#if __has_include(<react/config/ReactNativeConfig.h>)
#import <react/config/ReactNativeConfig.h>
#endif

#import <react/renderer/runtimescheduler/RuntimeScheduler.h>
#import <react/renderer/runtimescheduler/RuntimeSchedulerCallInvoker.h>
#import <React/RCTSurfacePresenter.h>
#import <React/RCTBridge+Private.h>
#import <React/RCTImageLoader.h>
#import <React/RCTBridgeProxy.h>
#import <React/RCTSurfacePresenter.h>
#import <react/utils/ManagedObjectWrapper.h>

#import <React/RCTComponentViewFactory.h>


static NSString *const kRNConcurrentRoot = @"concurrentRoot";

#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
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
    
#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
    [self _setUpFeatureFlags];
    #if RN_VERSION_MINOR == 77
        self.rootViewFactory = [self createRCTRootViewFactory];
    #else
        self.reactNativeFactory = [[RCTReactNativeFactory alloc] init];
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
    RCTEnableTurboModuleInteropBridgeProxy(YES);
    
    self.reactNativeFactory.rootViewFactory.reactHost = [self.reactNativeFactory.rootViewFactory createReactHost:launchOptions];
    
    [ReactNativeNavigation bootstrapWithHost:self.reactNativeFactory.rootViewFactory.reactHost];
#endif
    
    return YES;
}


#if RN_VERSION_MAJOR == 0 && RN_VERSION_MINOR < 79
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
