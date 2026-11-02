#import "ReactNativeNavigation.h"

#import <React/RCTUIManager.h>

#import "RNNBridgeManager.h"
#import "RNNDeepLinkDispatcher.h"
#import "RNNLayoutManager.h"
#import "RNNSplashScreenViewController.h"

@interface ReactNativeNavigation ()

#ifdef RCT_NEW_ARCH_ENABLED
@property(nonatomic, strong) RNNTurboManager *turboManager;
#endif

@property(nonatomic, strong) RNNBridgeManager *bridgeManager;

@property(nonatomic, strong) UIWindow *window;

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

#pragma mark - window

+ (UIWindow *)mainWindow {
    return [[ReactNativeNavigation sharedInstance] mainWindow];
}

+ (void)attachMainWindowToScene:(UIWindowScene *)scene {
    [[ReactNativeNavigation sharedInstance] attachMainWindowToScene:scene];
}

#pragma mark - deep linking

+ (void)dispatchDeepLinkURL:(NSURL *)url {
    [[RNNDeepLinkDispatcher sharedDispatcher] dispatchURL:url];
}

+ (void)dispatchOpenURLContexts:(NSSet<UIOpenURLContext *> *)URLContexts inScene:(UIScene *)scene {
    [[RNNDeepLinkDispatcher sharedDispatcher] dispatchOpenURLContexts:URLContexts scene:scene];
}

+ (void)dispatchUserActivity:(NSUserActivity *)userActivity inScene:(UIScene *)scene {
    [[RNNDeepLinkDispatcher sharedDispatcher] dispatchUserActivity:userActivity scene:scene];
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
    UIWindow *window = [self appDelegateWindow];
    if (window) {
        self.window = window;
        return window;
    }
    if (!self.window) {
        self.window = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
    }
    [self assignWindowToAppDelegate:self.window];
    return self.window;
}

- (void)attachMainWindowToScene:(UIWindowScene *)scene {
    UIWindow *window = [self appDelegateWindow] ?: self.window;
    if (window) {
        window.windowScene = scene;
        window.frame = scene.coordinateSpace.bounds;
    } else {
        window = [[UIWindow alloc] initWithWindowScene:scene];
    }
    self.window = window;
    [self assignWindowToAppDelegate:window];
    [window makeKeyAndVisible];
}

- (UIWindow *)appDelegateWindow {
    id<UIApplicationDelegate> appDelegate = UIApplication.sharedApplication.delegate;
    return [appDelegate respondsToSelector:@selector(window)] ? appDelegate.window : nil;
}

- (void)assignWindowToAppDelegate:(UIWindow *)window {
    id<UIApplicationDelegate> appDelegate = UIApplication.sharedApplication.delegate;
    if ([appDelegate respondsToSelector:@selector(setWindow:)]) {
        appDelegate.window = window;
    }
}

@end
