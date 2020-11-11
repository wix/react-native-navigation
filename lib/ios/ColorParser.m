#import "ColorParser.h"
#import "NoColor.h"
#import "NullColor.h"
#import <React/RCTConvert.h>

@implementation ColorParser

+ (Color *)parse:(NSDictionary *)json key:(NSString *)key {
    if ([json[key] isEqual:@"NoColor"])
        return [NoColor new];
    else if (json[key])
        return [Color withValue:[RCTConvert UIColor:json[key]]];

    return [NullColor new];
}

@end
