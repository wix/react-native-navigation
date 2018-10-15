#import "TextParser.h"
#import "NullText.h"
#import <React/RCTConvert.h>

@implementation TextParser

+ (Text *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] && ![json[key] isKindOfClass:[NSNull class]] ? [[Text alloc] initWithValue:[RCTConvert NSString:json[key]]] : [NullText new];
}

@end
