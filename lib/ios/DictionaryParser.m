#import "DictionaryParser.h"
#import "NullDictionary.h"

@implementation DictionaryParser

+ (Dictionary *)parse:(NSDictionary *)json key:(NSString *)key {
	return json[key] ? [[Dictionary alloc] initWithValue:json[key]] : [NullDictionary new];
}

@end
