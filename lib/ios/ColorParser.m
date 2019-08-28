#import "ColorParser.h"
#import "NullColor.h"
#import "NoColor.h"
#import <React/RCTConvert.h>

@implementation ColorParser

+ (Color *)parse:(NSDictionary *)json key:(NSString *)key {
	if (json[key]) {
		return [json[key] isKindOfClass:[NSNumber class]] ? [[Color alloc] initWithValue:[RCTConvert UIColor:json[key]]] : [NoColor new];
	}
	return [NullColor new];
}

@end
