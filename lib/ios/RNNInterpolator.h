#import <Foundation/Foundation.h>
#import "Interpolators/Interpolator.h"

@interface RNNInterpolator : NSObject

+ (UIColor *)fromColor:(UIColor *)fromColor toColor:(UIColor *)toColor precent:(CGFloat)precent;

+ (CGFloat)fromFloat:(CGFloat)from toFloat:(CGFloat)to precent:(CGFloat)precent interpolator:(id<Interpolator>)interpolator;

+ (CGRect)fromRect:(CGRect)from toRect:(CGRect)toRect precent:(CGFloat)precent interpolator:(id<Interpolator>)interpolator;

@end
