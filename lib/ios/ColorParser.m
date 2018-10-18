#import "ColorParser.h"
#import "NullColor.h"
#import <React/RCTConvert.h>

@implementation ColorParser

+ (Color *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] ? [[Color alloc] initWithValue:[RCTConvert UIColor:json[key]]] : [NullColor new];
}

@end
