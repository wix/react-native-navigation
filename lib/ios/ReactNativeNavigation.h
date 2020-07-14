#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary* props, RCTBridge* bridge);

@interface ReactNativeNavigation : NSObject

+ (void)bootstrapWithlaunchOptions:(NSDictionary *)launchOptions;

+ (void)bootstrapbootstrapWithLaunchOptions:(NSDictionary *)launchOptions andBridgeDelegate:(id<RCTBridgeDelegate>)bridgeDelegate;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

+ (UIViewController *)findViewController:(NSString *)componentId;

+ (RCTBridge *)getBridge;

@end
