#import "TextParser.h"
#import "NullText.h"
#import <React/RCTConvert.h>

@implementation TextParser

+ (Text *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] ? [[Text alloc] initWithValue:json[key]] : [NullText new];
}

@end
