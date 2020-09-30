#import "RNNInterpolator.h"
#import "Color+Interpolation.h"

@implementation RNNInterpolator

+ (UIColor *)fromColor:(UIColor *)fromColor toColor:(UIColor *)toColor precent:(CGFloat)precent {
    return [fromColor ?: UIColor.clearColor interpolateToValue:toColor ?: UIColor.clearColor progress:precent behavior:RNNInterpolationBehaviorUseLABColorSpace];
}

+ (CGFloat)fromFloat:(CGFloat)from toFloat:(CGFloat)to precent:(CGFloat)precent interpolator:(id<Interpolator>)interpolator {
    return RNNInterpolate(from, to, precent, interpolator);
}

+ (CGRect)fromRect:(CGRect)from toRect:(CGRect)to precent:(CGFloat)p interpolator:(id<Interpolator>)interpolator {
    return CGRectMake(RNNInterpolate(from.origin.x, to.origin.x, p, interpolator),
                      RNNInterpolate(from.origin.y, to.origin.y, p, interpolator),
                      RNNInterpolate(from.size.width, to.size.width, p, interpolator),
                      RNNInterpolate(from.size.height, to.size.height, p, interpolator));
}

static CGFloat RNNInterpolate(CGFloat from, CGFloat to, CGFloat p, id<Interpolator> interpolator) {
    return from + [interpolator interpolate:p] * (to - from);
}


@end
