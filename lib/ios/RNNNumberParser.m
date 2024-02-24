#import "RNNNumberParser.h"
#import "NullNumber.h"

@implementation RNNNumberParser

+ (RNNNumber *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[RNNNumber alloc] initWithValue:json[key]] : [NullNumber new];
}

@end
