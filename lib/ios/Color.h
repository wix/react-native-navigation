#import "RNNParam.h"
#import <UIKit/UIKit.h>

@interface Color : RNNParam

+ (instancetype)withColor:(UIColor *)value;

- (instancetype)initWithValue:(UIColor *)value;

- (UIColor *)get;

- (UIColor *)withDefault:(id)defaultValue;

@end
