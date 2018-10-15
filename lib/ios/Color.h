#import "Param.h"
#import <UIKit/UIKit.h>

@interface Color : Param

- (instancetype)initWithValue:(UIColor *)value;

- (UIColor *)get;

- (UIColor *)getWithDefaultValue:(id)defaultValue;

@end
