#import "RNNNumberParser.h"
#import "RNNNullNumber.h"

@implementation RNNNumberParser

+ (RNNNumber *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[RNNNumber alloc] initWithValue:json[key]] : [RNNNullNumber new];
}

@end
