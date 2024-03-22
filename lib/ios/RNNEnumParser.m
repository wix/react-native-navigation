#import "RNNEnumParser.h"
#import "RNNNullEnum.h"

@implementation RNNEnumParser

+ (RNNEnum *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)clazz {
    if (json[key]) {
        return [json[key] isKindOfClass:[NSString class]] ? [[clazz alloc] initWithValue:json[key]]
                                                          : [RNNNullEnum new];
    }
    return [RNNNullEnum new];
}

@end
