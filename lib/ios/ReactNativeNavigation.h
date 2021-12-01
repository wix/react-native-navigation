#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <UIKit/UIKit.h>

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary *props, RCTBridge *bridge);

@interface ReactNativeNavigation : NSObject

+ (void)bootstrapWithBridge:(RCTBridge *)bridge;

+ (void)bootstrapWithDelegate:(id<RCTBridgeDelegate>)bridgeDelegate
                launchOptions:(NSDictionary *)launchOptions
    __attribute__((deprecated("Use bootstrapWithBridge: instead")));

+ (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

+ (UIViewController *)findViewController:(NSString *)componentId;

+ (RCTBridge *)getBridge;

@end
