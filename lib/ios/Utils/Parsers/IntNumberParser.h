#import "IntNumber.h"
#import <Foundation/Foundation.h>

@interface IntNumberParser : NSObject

+ (IntNumber *)parse:(NSDictionary *)json key:(NSString *)key;

@end
