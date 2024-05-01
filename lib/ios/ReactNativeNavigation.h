#import <Foundation/Foundation.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import <UIKit/UIKit.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <React-RuntimeApple/ReactCommon/RCTHost.h>
#import "RNNTurboManager.h"
#endif

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary *props, RCTBridge *bridge);

@interface ReactNativeNavigation : NSObject

#ifdef RCT_NEW_ARCH_ENABLED
+ (void)bootstrapWithHost:(RCTHost *)host;
+ (void)registerExternalHostComponent:(NSString *)name callback:(RNNExternalHostViewCreator)callback;
+ (RCTHost *)getHost;
#endif

+ (void)bootstrapWithBridge:(RCTBridge *)bridge;

+ (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge;

+ (RCTBridge *)getBridge;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

+ (UIViewController *)findViewController:(NSString *)componentId;

@end
