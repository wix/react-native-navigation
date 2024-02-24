#import "RNNNumber.h"
#import <Foundation/Foundation.h>

@interface RNNNumberParser : NSObject

+ (RNNNumber *)parse:(NSDictionary *)json key:(NSString *)key;

@end
