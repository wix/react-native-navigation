#import "RNNEnumParser.h"
#import "NullEnum.h"

@implementation RNNEnumParser

+ (Enum *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)clazz {
    if (json[key]) {
        return [json[key] isKindOfClass:[NSString class]] ? [[clazz alloc] initWithValue:json[key]]
                                                          : [NullEnum new];
    }
    return [NullEnum new];
}

@end
