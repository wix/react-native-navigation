#import <Foundation/Foundation.h>
#import "Double.h"

@interface DoubleParser : NSObject

+ (Double *)parse:(NSDictionary *)json key:(NSString *)key;

@end
