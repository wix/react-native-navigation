#import "BoolParser.h"
#import "NullBool.h"

@implementation BoolParser

+ (Bool *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[Bool alloc] initWithValue:json[key]] : [NullBool new];
}

@end
