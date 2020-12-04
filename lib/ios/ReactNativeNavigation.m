#import "ReactNativeNavigation.h"

#import <React/RCTUIManager.h>

#import "RNNBridgeManager.h"
#import "RNNLayoutManager.h"
#import "RNNSplashScreen.h"

@interface ReactNativeNavigation ()

@property(nonatomic, strong) RNNBridgeManager *bridgeManager;

@end

@implementation ReactNativeNavigation

#pragma mark - public API

+ (void)bootstrapWithDelegate:(id<RCTBridgeDelegate>)bridgeDelegate
                launchOptions:(NSDictionary *)launchOptions {
    [[ReactNativeNavigation sharedInstance] bootstrapWithDelegate:bridgeDelegate
                                                    launchOptions:launchOptions];
}

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
    [[ReactNativeNavigation sharedInstance].bridgeManager registerExternalComponent:name
                                                                           callback:callback];
}

+ (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    return [[ReactNativeNavigation sharedInstance].bridgeManager extraModulesForBridge:bridge];
}

+ (RCTBridge *)getBridge {
    return [[ReactNativeNavigation sharedInstance].bridgeManager bridge];
}

+ (UIViewController *)findViewController:(NSString *)componentId {
    return [[ReactNativeNavigation sharedInstance].bridgeManager findComponentForId:componentId];
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

- (void)bootstrapWithDelegate:(id<RCTBridgeDelegate>)bridgeDelegate
                launchOptions:(NSDictionary *)launchOptions {
    UIWindow *mainWindow = [self initializeKeyWindowIfNeeded];

    self.bridgeManager = [[RNNBridgeManager alloc] initWithLaunchOptions:launchOptions
                                                       andBridgeDelegate:bridgeDelegate
                                                              mainWindow:mainWindow];
    [self.bridgeManager initializeBridge];
    [RNNSplashScreen showOnWindow:mainWindow];
}

- (UIWindow *)initializeKeyWindowIfNeeded {
    UIWindow *keyWindow = UIApplication.sharedApplication.delegate.window;
    if (!keyWindow) {
        keyWindow = [[UIWindow alloc] initWithFrame:[UIScreen mainScreen].bounds];
        UIApplication.sharedApplication.delegate.window = keyWindow;
    }
    return keyWindow;
}

@end
