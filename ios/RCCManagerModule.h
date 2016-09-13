#import <Foundation/Foundation.h>
#import "RCTBridgeModule.h"
#import <UIKit/UIKit.h>


@interface RCCManagerModule : NSObject <RCTBridgeModule>
+(void)cancelAllRCCViewControllerReactTouches;
@end
