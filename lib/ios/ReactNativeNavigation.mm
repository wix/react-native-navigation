#import "ReactNativeNavigation.h"

#import <React/RCTUIManager.h>

#import "RNNBridgeManager.h"
#import "RNNLayoutManager.h"
#import "RNNSplashScreenViewController.h"

@interface ReactNativeNavigation ()

#ifdef RCT_NEW_ARCH_ENABLED
@property(nonatomic, strong) RNNTurboManager *turboManager;
#endif

@property(nonatomic, strong) RNNBridgeManager *bridgeManager;

@end

@implementation ReactNativeNavigation

#pragma mark - public API

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
	[[ReactNativeNavigation sharedInstance].bridgeManager registerExternalComponent:name
																		   callback:callback];
}

+ (UIViewController *)findViewController:(NSString *)componentId {
#ifdef RCT_NEW_ARCH_ENABLED
	if ([ReactNativeNavigation sharedInstance].turboManager == nil) {
		return [[ReactNativeNavigation sharedInstance].bridgeManager findComponentForId:componentId];
	} else {
		return [[ReactNativeNavigation sharedInstance].turboManager findComponentForId:componentId];
	}
#else
	return [[ReactNativeNavigation sharedInstance].bridgeManager findComponentForId:componentId];
#endif
}

#pragma mark - turbomodules

#ifdef RCT_NEW_ARCH_ENABLED
+ (void)bootstrapWithHost:(RCTHost *)host {
	[[ReactNativeNavigation sharedInstance] bootstrapWithHost:host];
}

+ (void)registerExternalHostComponent:(NSString *)name callback:(RNNExternalHostViewCreator)callback {
	[[ReactNativeNavigation sharedInstance].turboManager registerExternalComponent:name
                                                                           callback:callback];
}

- (void)bootstrapWithHost:(RCTHost *)host {
	self.turboManager = [[RNNTurboManager alloc] initWithHost:host mainWindow:[self mainWindow]];
	[RNNSplashScreenViewController showOnWindow:[self mainWindow]];
}

+ (RCTHost *)getHost {
	if ([ReactNativeNavigation sharedInstance].turboManager == nil) {
		return nil;
	}

	return [[ReactNativeNavigation sharedInstance].turboManager host];
}
#endif

#pragma mark - bridge

+ (void)bootstrapWithBridge:(RCTBridge *)bridge {
	[[ReactNativeNavigation sharedInstance] bootstrapWithBridge:bridge];
}
// gets called when the Bridge is created, implicitly initializes the RNNBridgeManager.
+ (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    RNNBridgeManager *manager =
        [[ReactNativeNavigation sharedInstance] getBridgeManagerForBridge:bridge];
    return [manager extraModulesForBridge:bridge];
}

+ (RCTBridge *)getBridge {
    return [[ReactNativeNavigation sharedInstance].bridgeManager bridge];
}

- (RNNBridgeManager *)getBridgeManagerForBridge:(RCTBridge *)bridge {
	if (self.bridgeManager == nil) {
		self.bridgeManager = [[RNNBridgeManager alloc] initWithBridge:bridge
														   mainWindow:[self mainWindow]];
	}
	return self.bridgeManager;
}

- (void)bootstrapWithBridge:(RCTBridge *)bridge {
	[RNNSplashScreenViewController showOnWindow:[self mainWindow]];
}

#pragma mark - instance

+ (instancetype)sharedInstance {
    static ReactNativeNavigation *instance = nil;
    static dispatch_once_t onceToken = 0;
    dispatch_once(&onceToken, ^{
      if (instance == nil) {
          instance = [[ReactNativeNavigation alloc] init];
      }
    });

    return instance;
}

- (UIWindow *)mainWindow {
    UIWindow *keyWindow = UIApplication.sharedApplication.delegate.window;
    if (!keyWindow) {
        keyWindow = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
        UIApplication.sharedApplication.delegate.window = keyWindow;
    }
    return keyWindow;
}

@end
