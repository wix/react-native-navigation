#import "RNNColorParser.h"
#import "NoColor.h"
#import "RNNNullColor.h"
#import <React/RCTConvert.h>

@implementation RNNColorParser

+ (RNNColor *)parse:(NSDictionary *)json key:(NSString *)key {
    if ([json[key] isEqual:@"NoColor"])
        return [NoColor new];
    else if (json[key])
        return [Color withValue:[RCTConvert UIColor:json[key]]];

    return [RNNNullColor new];
}

@end
