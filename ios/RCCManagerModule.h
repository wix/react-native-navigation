#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>
#import <UIKit/UIKit.h>


@interface RCCManagerModule : NSObject <RCTBridgeModule>
+(void)cancelAllRCCViewControllerReactTouches;
@end
