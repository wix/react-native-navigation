#import "DoubleParser.h"
#import "NullDouble.h"

@implementation DoubleParser

+ (Double *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[Double alloc] initWithValue:json[key]] : [NullDouble new];
}

@end
