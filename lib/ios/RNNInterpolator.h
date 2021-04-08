#import "Interpolator.h"
#import <Foundation/Foundation.h>

@interface RNNInterpolator : NSObject

+ (CGPoint)fromPoint:(CGPoint)from
             toPoint:(CGPoint)to
             precent:(CGFloat)p
        interpolator:(id<Interpolator>)interpolator;

+ (UIColor *)fromColor:(UIColor *)fromColor toColor:(UIColor *)toColor precent:(CGFloat)precent;

+ (CGFloat)fromFloat:(CGFloat)from
             toFloat:(CGFloat)to
             precent:(CGFloat)precent
        interpolator:(id<Interpolator>)interpolator;

+ (CGRect)fromRect:(CGRect)from
            toRect:(CGRect)toRect
           precent:(CGFloat)precent
      interpolator:(id<Interpolator>)interpolator;

+ (CATransform3D)fromTransform:(CATransform3D)from
                   toTransform:(CATransform3D)to
                       precent:(CGFloat)p
                  interpolator:(id<Interpolator>)interpolator;

@end
