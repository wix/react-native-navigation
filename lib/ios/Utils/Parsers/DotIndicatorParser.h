#import <Foundation/Foundation.h>

@class DotIndicatorOptions;

@interface DotIndicatorParser : NSObject
+ (DotIndicatorOptions *)parse:(NSDictionary *)dict;
@end