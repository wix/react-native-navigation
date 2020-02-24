#import <Foundation/Foundation.h>
#import "RCTConvert+Interpolation.h"

@interface RNNInterpolator : NSObject

+ (UIColor *)fromColor:(UIColor *)fromColor toColor:(UIColor *)toColor precent:(CGFloat)precent;

+ (CGFloat)fromFloat:(CGFloat)from toFloat:(CGFloat)to precent:(CGFloat)precent interpolation:(RNNInterpolationOptions)interpolation;

+ (CGRect)fromRect:(CGRect)from toRect:(CGRect)toRect precent:(CGFloat)precent interpolation:(RNNInterpolationOptions)interpolation;

@end
