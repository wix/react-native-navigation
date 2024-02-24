#import "RNNIntNumberParser.h"
#import "NullIntNumber.h"

@implementation RNNIntNumberParser

+ (RNNIntNumber *)parse:(NSDictionary *)json
					key:(NSString *)key {
    return json[key] ? [[RNNIntNumber alloc] initWithValue:json[key]] : [NullIntNumber new];
}

@end
