#import <Foundation/Foundation.h>
#import <React/RCTBridgeModule.h>

typedef enum RNNCommandsErrorCodes {
	RNNCommandErrorCodeNoStack = 0
} RNNCommandsErrorCodes;

@interface RNNErrorHandler : NSObject

+ (void)reject:(RCTPromiseRejectBlock)reject withErrorCode:(NSInteger)errorCode errorDescription:(NSString*)errorDescription;
+ (NSString *)getCallerFunctionName;

@end
