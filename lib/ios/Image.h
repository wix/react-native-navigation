#import "RNNParam.h"
#import <UIKit/UIKit.h>

@interface Image : RNNParam

- (UIImage *)get;

- (UIImage *)withDefault:(UIImage *)defaultValue;

@end
