#import "RNNDoubleParser.h"
#import "RNNNullDouble.h"

@implementation RNNDoubleParser

+ (RNNDouble *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[RNNDouble alloc] initWithValue:json[key]] : [RNNNullDouble new];
}

@end
