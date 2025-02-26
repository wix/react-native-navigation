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
#import <React/RCTSurfacePresenter.h>
#import <react/utils/ManagedObjectWrapper.h>

static NSString *const kRNConcurrentRoot = @"concurrentRoot";

@interface RNNAppDelegate () <RCTTurboModuleManagerDelegate, RCTCxxBridgeDelegate> {}
@end

#endif

@implementation RNNAppDelegate

- (BOOL)application:(UIApplication *)application
	didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {

	// Copied from RCTAppDelegate, it private inside it
	self.rootViewFactory = [self createRCTRootViewFactory];

#ifdef RCT_NEW_ARCH_ENABLED
	RCTAppSetupPrepareApp(application, self.turboModuleEnabled);
	RCTSetNewArchEnabled(TRUE);

	if (self.bridgelessEnabled) {
		// Creating host instead of bridge
		self.rootViewFactory.reactHost = [self.rootViewFactory createReactHost:launchOptions];

		[ReactNativeNavigation bootstrapWithHost:self.rootViewFactory.reactHost];

		return YES;
	}
#endif
	// Force RN to init all necessary modules instead of copy-pasting code (ex. bridge)
	[self.rootViewFactory viewWithModuleName:@""];

	[ReactNativeNavigation bootstrapWithBridge:self.bridge];

	return YES;
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
													   newArchEnabled:self.fabricEnabled
												   turboModuleEnabled:self.turboModuleEnabled
													bridgelessEnabled:self.bridgelessEnabled];

  if (!self.bridgelessEnabled) {
	  configuration.extraModulesForBridge = ^NSArray<id<RCTBridgeModule>> * _Nonnull(RCTBridge * _Nonnull bridge) {
		  return [ReactNativeNavigation extraModulesForBridge:bridge];
	  };
  }

  configuration.createRootViewWithBridge = ^UIView *(RCTBridge *bridge, NSString *moduleName, NSDictionary *initProps)
  {
	// Ignoring creation of default view, will initialize our custom UIViewController later
	return nil;
  };

  configuration.createBridgeWithDelegate = ^RCTBridge *(id<RCTBridgeDelegate> delegate, NSDictionary *launchOptions)
  {
	return [weakSelf createBridgeWithDelegate:delegate launchOptions:launchOptions];
  };

#ifndef RCT_NEW_ARCH_ENABLED
	return [[RCTRootViewFactory alloc] initWithConfiguration:configuration];
  }
#else
  return [[RCTRootViewFactory alloc] initWithConfiguration:configuration andTurboModuleManagerDelegate:self];
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
