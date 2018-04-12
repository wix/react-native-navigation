#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>
#import <React/RCTBridge.h>

typedef UIViewController * (^RNNExternalViewCreator)(NSDictionary* props, RCTBridge* bridge);

@interface ReactNativeNavigation : NSObject

+ (void)bootstrap:(NSURL *)jsCodeLocation launchOptions:(NSDictionary *)launchOptions;

+ (void)registerExternalComponent:(NSString *)name callback:(RNNExternalViewCreator)callback;

+ (RCTBridge *)getBridge;

@end
