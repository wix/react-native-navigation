#import "RNNInterpolator.h"
#import "Color+Interpolation.h"

@implementation RNNInterpolator

+ (CGPoint)fromPoint:(CGPoint)from
             toPoint:(CGPoint)to
             precent:(CGFloat)p
        interpolator:(id<Interpolator>)interpolator {
    return CGPointMake(RNNInterpolate(from.x, to.x, p, interpolator),
                       RNNInterpolate(from.y, to.y, p, interpolator));
}

+ (UIColor *)fromColor:(UIColor *)fromColor toColor:(UIColor *)toColor precent:(CGFloat)precent {
    return [fromColor ?: UIColor.clearColor
        interpolateToValue:toColor ?: UIColor.clearColor
                  progress:precent
                  behavior:RNNInterpolationBehaviorUseLABColorSpace];
}

+ (CGFloat)fromFloat:(CGFloat)from
             toFloat:(CGFloat)to
             precent:(CGFloat)precent
        interpolator:(id<Interpolator>)interpolator {
    return RNNInterpolate(from, to, precent, interpolator);
}

+ (CGRect)fromRect:(CGRect)from
            toRect:(CGRect)to
           precent:(CGFloat)p
      interpolator:(id<Interpolator>)interpolator {
    return CGRectMake(RNNInterpolate(from.origin.x, to.origin.x, p, interpolator),
                      RNNInterpolate(from.origin.y, to.origin.y, p, interpolator),
                      RNNInterpolate(from.size.width, to.size.width, p, interpolator),
                      RNNInterpolate(from.size.height, to.size.height, p, interpolator));
}

+ (CATransform3D)fromTransform:(CATransform3D)from
                   toTransform:(CATransform3D)to
                       precent:(CGFloat)p
                  interpolator:(id<Interpolator>)interpolator {
    CATransform3D transform = CATransform3DIdentity;

    transform.m11 = [RNNInterpolator fromFloat:from.m11
                                       toFloat:to.m11
                                       precent:p
                                  interpolator:interpolator];
    transform.m12 = [RNNInterpolator fromFloat:from.m12
                                       toFloat:to.m12
                                       precent:p
                                  interpolator:interpolator];
    transform.m13 = [RNNInterpolator fromFloat:from.m13
                                       toFloat:to.m13
                                       precent:p
                                  interpolator:interpolator];
    transform.m14 = [RNNInterpolator fromFloat:from.m14
                                       toFloat:to.m14
                                       precent:p
                                  interpolator:interpolator];

    transform.m21 = [RNNInterpolator fromFloat:from.m21
                                       toFloat:to.m21
                                       precent:p
                                  interpolator:interpolator];
    transform.m22 = [RNNInterpolator fromFloat:from.m22
                                       toFloat:to.m22
                                       precent:p
                                  interpolator:interpolator];
    transform.m23 = [RNNInterpolator fromFloat:from.m23
                                       toFloat:to.m23
                                       precent:p
                                  interpolator:interpolator];
    transform.m24 = [RNNInterpolator fromFloat:from.m24
                                       toFloat:to.m24
                                       precent:p
                                  interpolator:interpolator];

    transform.m31 = [RNNInterpolator fromFloat:from.m31
                                       toFloat:to.m31
                                       precent:p
                                  interpolator:interpolator];
    transform.m32 = [RNNInterpolator fromFloat:from.m32
                                       toFloat:to.m32
                                       precent:p
                                  interpolator:interpolator];
    transform.m33 = [RNNInterpolator fromFloat:from.m33
                                       toFloat:to.m33
                                       precent:p
                                  interpolator:interpolator];
    transform.m34 = [RNNInterpolator fromFloat:from.m34
                                       toFloat:to.m34
                                       precent:p
                                  interpolator:interpolator];

    transform.m41 = [RNNInterpolator fromFloat:from.m41
                                       toFloat:to.m41
                                       precent:p
                                  interpolator:interpolator];
    transform.m42 = [RNNInterpolator fromFloat:from.m42
                                       toFloat:to.m42
                                       precent:p
                                  interpolator:interpolator];
    transform.m43 = [RNNInterpolator fromFloat:from.m43
                                       toFloat:to.m43
                                       precent:p
                                  interpolator:interpolator];
    transform.m44 = [RNNInterpolator fromFloat:from.m44
                                       toFloat:to.m44
                                       precent:p
                                  interpolator:interpolator];

    return transform;
}

/**
 Lerp Function for float values. The formula for lerping numbers is `from + (to
 - from) * fraction`, where fraction represents the percent the animation has
 completed.
 */
static CGFloat RNNInterpolate(CGFloat from, CGFloat to, CGFloat p, id<Interpolator> interpolator) {
    return from + [interpolator interpolate:p] * (to - from);
}

@end
