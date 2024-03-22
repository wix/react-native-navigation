#import "RNNTextParser.h"
#import "RNNNullText.h"
#import <React/RCTConvert.h>

@implementation RNNTextParser

+ (RNNText *)parse:(NSDictionary *)json key:(NSString *)key {
    return json[key] ? [[RNNText alloc] initWithValue:json[key]] : [RNNNullText new];
}

@end
