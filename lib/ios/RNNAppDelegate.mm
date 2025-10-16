
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

#import <React-RCTAppDelegate/RCTAppDelegate.h>

#if __has_include(<React-RCTAppDelegate/RCTReactNativeFactory.h>)
#import <React-RCTAppDelegate/RCTReactNativeFactory.h>
#elif __has_include(<React_RCTAppDelegate/RCTReactNativeFactory.h>)
#import <React_RCTAppDelegate/RCTReactNativeFactory.h>
// RN 0.77 support
#define RN077
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

@interface RNNAppDelegate () <RCTTurboModuleManagerDelegate,
                              RCTComponentViewFactoryComponentProvider> {
}
@end

@implementation RNNAppDelegate

- (BOOL)application:(UIApplication *)application
    didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

#ifdef RN077
    [self _setUpFeatureFlags];
    self.rootViewFactory = [self createRCTRootViewFactory];
    [RCTComponentViewFactory currentComponentViewFactory].thirdPartyFabricComponentsProvider = self;
    RCTAppSetupPrepareApp(application, self.newArchEnabled);
    RCTSetNewArchEnabled(TRUE);
#else
    self.reactNativeFactory = [RCTReactNativeFactory new];
    self.reactNativeFactory = [self.reactNativeFactory initWithDelegate:self];
#endif
    
    RCTEnableTurboModuleInterop(YES);
    RCTEnableTurboModuleInteropBridgeProxy(YES);

    self.rootViewFactory.reactHost = [self.rootViewFactory createReactHost:launchOptions];

    [ReactNativeNavigation bootstrapWithHost:self.rootViewFactory.reactHost];

    return YES;
}

- (NSURL *)sourceURLForBridge:(RCTBridge *)bridge {
	[NSException raise:@"RCTBridgeDelegate::sourceURLForBridge not implemented"
				format:@"Subclasses must implement a valid sourceURLForBridge method"];
	return nil;
}

- (BOOL)concurrentRootEnabled {
	return true;
}



#ifdef RN077
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

