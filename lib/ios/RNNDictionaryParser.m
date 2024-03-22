#import "RNNDictionaryParser.h"
#import "RNNNullDictionary.h"

@implementation RNNDictionaryParser

+ (RNNDictionary *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[RNNDictionary alloc] initWithValue:json[key]] : [RNNNullDictionary new];
}

@end
