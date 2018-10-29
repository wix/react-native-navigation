#import "NumberParser.h"
#import "NullNumber.h"

@implementation NumberParser

+ (Number *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] ? [[Number alloc] initWithValue:json[key]] : [NullNumber new];
}

@end
