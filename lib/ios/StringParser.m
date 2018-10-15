#import "StringParser.h"
#import "NullString.h"
#import <React/RCTConvert.h>

@implementation StringParser

+ (String *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] ? [[String alloc] initWithValue:[RCTConvert NSString:json[key]]] : [NullString new];
}

@end
