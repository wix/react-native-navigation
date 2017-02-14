#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

#if __has_include(<React/RCTBridgeModule.h>)
#import <React/RCTBridgeModule.h>
#elif __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#elif __has_include("React/RCTBridgeModule.h")
#import "React/RCTBridgeModule.h"   // Required when used as a Pod in a Swift project
#endif

@interface RCCManagerModule : NSObject <RCTBridgeModule>
+(void)cancelAllRCCViewControllerReactTouches;
@end
