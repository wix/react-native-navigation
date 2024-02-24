#import "RNNBoolParser.h"
#import "NullBool.h"

@implementation RNNBoolParser

+ (Bool *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[Bool alloc] initWithValue:json[key]] : [NullBool new];
}

@end
