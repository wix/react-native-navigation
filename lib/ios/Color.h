#import "Param.h"
#import <UIKit/UIKit.h>

@interface Color : Param

- (UIColor *)get;

- (UIColor *)getWithDefaultValue:(id)defaultValue;

@end
