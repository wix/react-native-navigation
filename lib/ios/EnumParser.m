#import "EnumParser.h"
#import "NullEnum.h"

@implementation EnumParser

+ (Enum *)parse:(NSDictionary *)json key:(NSString *)key ofClass:(Class)class {
	if (json[key]) {
		return [json[key] isKindOfClass:[NSString class]] ? [[class alloc] initWithValue:json[key]] : [NullEnum new];
	}
	return [NullEnum new];
}

@end
