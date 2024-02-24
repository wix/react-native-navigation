#import <Foundation/Foundation.h>

@class RNNDotIndicatorOptions;

@interface RNNDotIndicatorParser : NSObject
+ (RNNDotIndicatorOptions *)parse:(NSDictionary *)dict;
@end
