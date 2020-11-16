#import "IntNumberParser.h"
#import "NullIntNumber.h"

@implementation IntNumberParser

+ (IntNumber *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[IntNumber alloc] initWithValue:json[key]] : [NullIntNumber new];
}

@end
