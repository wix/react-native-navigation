#import "RNNIntNumber.h"
#import <Foundation/Foundation.h>

@interface RNNIntNumberParser : NSObject

+ (RNNIntNumber *)parse:(NSDictionary *)json key:(NSString *)key;

@end
