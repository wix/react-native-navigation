#import "RNNParam.h"
#import <UIKit/UIKit.h>

@interface RNNImage : RNNParam

- (UIImage *)get;

- (UIImage *)withDefault:(UIImage *)defaultValue;

@end
