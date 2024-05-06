#import "RNNAppDelegate.h"
#import <ReactNativeNavigation/ReactNativeNavigation.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import "RCTAppSetupUtils.h"
#import <React/CoreModulesPlugins.h>
#import <React/RCTCxxBridgeDelegate.h>
#import <React/RCTLegacyViewManagerInteropComponentView.h>
#import <React/RCTSurfacePresenter.h>
#import <React/RCTSurfacePresenterStub.h>
#import <React/RCTSurfacePresenterBridgeAdapter.h>
#import <ReactCommon/RCTTurboModuleManager.h>
#import <react/config/ReactNativeConfig.h>
#import <react/renderer/runtimescheduler/RuntimeScheduler.h>
#import <react/renderer/runtimescheduler/RuntimeSchedulerCallInvoker.h>
#import <React/RCTSurfacePresenter.h>
#import <React/RCTBridge+Private.h>
#import <React/RCTImageLoader.h>
#import <React/RCTBridgeProxy.h>
#import <react/utils/ManagedObjectWrapper.h>

static NSString *const kRNConcurrentRoot = @"concurrentRoot";

@interface RNNAppDelegate () <RCTTurboModuleManagerDelegate, RCTCxxBridgeDelegate> {
    std::shared_ptr<const facebook::react::ReactNativeConfig> _reactNativeConfig;
    facebook::react::ContextContainer::Shared _contextContainer;
    facebook::react::RuntimeExecutor _runtimeExecutor;
    std::shared_ptr<facebook::react::RuntimeScheduler> _runtimeScheduler;
}
@end

#endif

@implementation RNNAppDelegate

#ifdef RCT_NEW_ARCH_ENABLED
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
    
#ifdef RCT_NEW_ARCH_ENABLED
    self.rootViewFactory = [self createRCTRootViewFactory];
    
    RCTAppSetupPrepareApp(application, self.turboModuleEnabled);
    
    if (self.bridgelessEnabled) {
        self.rootViewFactory.reactHost = [self.rootViewFactory createReactHost:launchOptions];
        
        [ReactNativeNavigation bootstrapWithHost:self.rootViewFactory.reactHost];
        
        return YES;
    }
#else
    self.bridge = [[RCTBridge alloc] initWithDelegate:self launchOptions:launchOptions];
#endif
    
    [ReactNativeNavigation bootstrapWithBridge:self.bridge];
    
    return YES;
}

- (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    return [ReactNativeNavigation extraModulesForBridge:bridge];
}

#ifdef RCT_NEW_ARCH_ENABLED
- (RCTRootViewFactory *)createRCTRootViewFactory
{
  __weak __typeof(self) weakSelf = self;
  RCTBundleURLBlock bundleUrlBlock = ^{
    RCTAppDelegate *strongSelf = weakSelf;
    return strongSelf.bundleURL;
  };

  RCTRootViewFactoryConfiguration *configuration =
      [[RCTRootViewFactoryConfiguration alloc] initWithBundleURLBlock:bundleUrlBlock
                                                       newArchEnabled:self.fabricEnabled
                                                   turboModuleEnabled:self.turboModuleEnabled
                                                    bridgelessEnabled:self.bridgelessEnabled];

  configuration.createRootViewWithBridge = ^UIView *(RCTBridge *bridge, NSString *moduleName, NSDictionary *initProps)
  {
    return [weakSelf createRootViewWithBridge:bridge moduleName:moduleName initProps:initProps];
  };

  configuration.createBridgeWithDelegate = ^RCTBridge *(id<RCTBridgeDelegate> delegate, NSDictionary *launchOptions)
  {
    return [weakSelf createBridgeWithDelegate:delegate launchOptions:launchOptions];
  };

  return [[RCTRootViewFactory alloc] initWithConfiguration:configuration andTurboModuleManagerDelegate:self];
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
