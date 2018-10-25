#import "Param.h"
#import <UIKit/UIKit.h>

@interface Image : Param

- (UIImage *)get;

- (UIImage *)getWithDefaultValue:(UIImage *)defaultValue;

@end
