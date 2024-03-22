#import "RNNDotIndicatorParser.h"
#import "RNNDotIndicatorOptions.h"

@implementation RNNDotIndicatorParser
+ (RNNDotIndicatorOptions *)parse:(NSDictionary *)dict {
    return [[RNNDotIndicatorOptions alloc] initWithDict:dict[@"dotIndicator"]];
}

@end
