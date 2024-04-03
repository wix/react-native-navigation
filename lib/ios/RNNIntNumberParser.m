#import "RNNIntNumberParser.h"
#import "RNNNullIntNumber.h"

@implementation RNNIntNumberParser

+ (RNNIntNumber *)parse:(NSDictionary *)json
					key:(NSString *)key {
    return json[key] ? [[RNNIntNumber alloc] initWithValue:json[key]] : [RNNNullIntNumber new];
}

@end
