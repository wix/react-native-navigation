#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary* props, RCTBridge* bridge);

@interface ReactNativeNavigation : NSObject

+ (void)bootstrapWithDelegate:(id<RCTBridgeDelegate>)bridgeDelegate launchOptions:(NSDictionary *)launchOptions;

+ (NSArray<id<RCTBridgeModule>> *)extraModulesForBridge:(RCTBridge *)bridge;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

+ (UIViewController *)findViewController:(NSString *)componentId;

+ (RCTBridge *)getBridge;

@end
