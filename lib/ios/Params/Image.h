#import "Param.h"
#import <UIKit/UIKit.h>

@interface Image : Param

- (UIImage *)get;

- (UIImage *)withDefault:(UIImage *)defaultValue;

@end
