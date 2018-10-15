#import <Foundation/Foundation.h>
#import "IntNumber.h"

@interface IntNumberParser : NSObject

+ (IntNumber *)parse:(NSDictionary *)json key:(NSString *)key;

@end
