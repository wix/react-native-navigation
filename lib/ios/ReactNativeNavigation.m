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

+ (void)bootstrapWithBridge:(RCTBridge *)bridge {
    [[ReactNativeNavigation sharedInstance] bootstrapWithBridge:bridge];
}

+ (void)bootstrapWithDelegate:(id<RCTBridgeDelegate>)bridgeDelegate
                launchOptions:(NSDictionary *)launchOptions {
    RCTBridge *bridge = [[RCTBridge alloc] initWithDelegate:bridgeDelegate
                                              launchOptions:launchOptions];
    [[ReactNativeNavigation sharedInstance] bootstrapWithBridge:bridge];
}

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback {
    [[ReactNativeNavigation sharedInstance].bridgeManager registerExternalComponent:name
                                                                           callback:callback];
}

// gets called when the Bridge is created, implicitly initializes the RNNBridgeManager.
+ (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge {
    RNNBridgeManager *bridgeManager =
        [[ReactNativeNavigation sharedInstance] getBridgeManagerForBridge:bridge];
    return [bridgeManager extraModulesForBridge:bridge];
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

- (void)bootstrapWithBridge:(RCTBridge *)bridge {
    [RNNSplashScreen showOnWindow:[self mainWindow]];
}

- (RNNBridgeManager *)getBridgeManagerForBridge:(RCTBridge *)bridge {
    if (self.bridgeManager == nil) {
        self.bridgeManager = [[RNNBridgeManager alloc] initWithBridge:bridge
                                                           mainWindow:[self mainWindow]];
    }
    return self.bridgeManager;
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
