#import "RNNTimeIntervalParser.h"

@implementation RNNTimeIntervalParser

+ (RNNTimeInterval *)parse:(NSDictionary *)json 
					   key:(NSString *)key {
    return [[RNNTimeInterval alloc] initWithValue:json[key]];
}

@end
