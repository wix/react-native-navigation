#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>
#import <React/RCTUIManager.h>
#import "RNNBridgeManagerDelegate.h"

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary* props, RCTBridge* bridge);

@interface ReactNativeNavigation : NSObject

+ (void)bootstrap:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions;

+ (void)bootstrap:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions bridgeManagerDelegate:(id<RNNBridgeManagerDelegate>)delegate;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

+ (UIViewController *)findViewController:(NSString *)componentId;

+ (void)setJSCodeLocation:(NSURL *)jsCodeLocation;

+ (RCTBridge *)getBridge;

@end
