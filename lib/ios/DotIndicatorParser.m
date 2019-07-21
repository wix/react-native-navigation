#import "DotIndicatorParser.h"
#import "DotIndicatorOptions.h"


@implementation DotIndicatorParser
+ (DotIndicatorOptions *)parse:(NSDictionary *)dict {
    return [[DotIndicatorOptions alloc] initWithDict:dict[@"dotIndicator"]];
}

@end