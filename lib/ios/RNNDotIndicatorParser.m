#import "RNNDotIndicatorParser.h"
#import "DotIndicatorOptions.h"

@implementation RNNDotIndicatorParser
+ (DotIndicatorOptions *)parse:(NSDictionary *)dict {
    return [[DotIndicatorOptions alloc] initWithDict:dict[@"dotIndicator"]];
}

@end
