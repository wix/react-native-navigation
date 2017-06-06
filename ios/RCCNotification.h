
#import <Foundation/Foundation.h>
#import "RCCManager.h"

@interface RCCNotification : NSObject
+(void)showWithParams:(NSDictionary*)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;
+(void)dismissWithParams:(NSDictionary*)params resolver:(RCTPromiseResolveBlock)resolve rejecter:(RCTPromiseRejectBlock)reject;
@end
