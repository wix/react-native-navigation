#import "StringParser.h"
#import "NullString.h"

@implementation StringParser

+ (String *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] ? [[String alloc] initWithValue:json[key]] : [NullString new];
}

@end
