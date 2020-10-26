#import "TimeInterval.h"
#import <Foundation/Foundation.h>

@interface TimeIntervalParser : NSObject

+ (TimeInterval *)parse:(NSDictionary *)json key:(NSString *)key;

@end
