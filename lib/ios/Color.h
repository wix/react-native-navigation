#import "Param.h"
#import <UIKit/UIKit.h>

@interface Color : Param

+ (instancetype)withColor:(UIColor *)value;

- (instancetype)initWithValue:(UIColor *)value;

- (UIColor *)get;

- (UIColor *)withDefault:(id)defaultValue;

@end
