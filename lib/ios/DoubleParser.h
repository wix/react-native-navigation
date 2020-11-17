#import "Double.h"
#import <Foundation/Foundation.h>

@interface DoubleParser : NSObject

+ (Double *)parse:(NSDictionary *)json key:(NSString *)key;

@end
