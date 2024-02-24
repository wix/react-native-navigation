#import <Foundation/Foundation.h>

@class DotIndicatorOptions;

@interface RNNDotIndicatorParser : NSObject
+ (DotIndicatorOptions *)parse:(NSDictionary *)dict;
@end
