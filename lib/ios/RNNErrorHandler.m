#import "RNNErrorHandler.h"

static NSString *const domain = @"com.reactnativenavigation";

@implementation RNNErrorHandler

+ (void)reject:(RCTPromiseRejectBlock)reject
       withErrorCode:(NSInteger)errorCode
    errorDescription:(NSString *)errorDescription {
    NSError *error =
        [NSError errorWithDomain:domain
                            code:errorCode
                        userInfo:@{NSLocalizedFailureReasonErrorKey : errorDescription}];
    if (reject) {
        reject([NSString stringWithFormat:@"%ld", (long)errorCode], errorDescription, error);
    }
}

+ (NSString *)getCallerFunctionName {
    NSString *sourceString = [[NSThread callStackSymbols] objectAtIndex:2];
    NSCharacterSet *separatorSet = [NSCharacterSet characterSetWithCharactersInString:@" -[]+?.,"];
    NSMutableArray *array = [NSMutableArray
        arrayWithArray:[sourceString componentsSeparatedByCharactersInSet:separatorSet]];
    [array removeObject:@""];
    return [[[array objectAtIndex:4] componentsSeparatedByString:@":"] objectAtIndex:0];
}

@end
