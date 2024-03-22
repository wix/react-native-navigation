#import "RNNBoolParser.h"
#import "RNNNullBool.h"

@implementation RNNBoolParser

+ (RNNBool *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[RNNBool alloc] initWithValue:json[key]] : [RNNNullBool new];
}

@end
