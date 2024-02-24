#import "RNNDictionaryParser.h"
#import "NullDictionary.h"

@implementation RNNDictionaryParser

+ (RNNDictionary *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[RNNDictionary alloc] initWithValue:json[key]] : [NullDictionary new];
}

@end
