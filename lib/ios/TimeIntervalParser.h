#import <Foundation/Foundation.h>
#import "TimeInterval.h"

@interface TimeIntervalParser : NSObject

+ (TimeInterval *)parse:(NSDictionary *)json key:(NSString *)key;

@end
