#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import <React/RCTBridgeDelegate.h>

@class RCTSurfacePresenterBridgeAdapter;
@class RCTTurboModuleManager;

@interface RNNAppDelegate : UIResponder <UIApplicationDelegate, RCTBridgeDelegate>

@property(nonatomic, strong) UIWindow *window;

#if RCT_NEW_ARCH_ENABLED

/// The TurboModule manager
@property(nonatomic, strong) RCTTurboModuleManager *turboModuleManager;
@property(nonatomic, strong) RCTSurfacePresenterBridgeAdapter *bridgeAdapter;

#endif

@end
