#import "TimeIntervalParser.h"

@implementation TimeIntervalParser

+ (TimeInterval *)parse:(NSDictionary *)json key:(NSString *)key {
    return [[TimeInterval alloc] initWithValue:json[key]];
}

@end
