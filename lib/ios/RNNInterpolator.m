#import "RNNInterpolator.h"
#import "Color+Interpolation.h"

@implementation RNNInterpolator

+ (UIColor *)fromColor:(UIColor *)fromColor toColor:(UIColor *)toColor precent:(CGFloat)precent {
    return [fromColor ?: UIColor.clearColor interpolateToValue:toColor ?: UIColor.clearColor progress:precent behavior:RNNInterpolationBehaviorUseLABColorSpace];
}

+ (CGFloat)fromFloat:(CGFloat)from toFloat:(CGFloat)to precent:(CGFloat)precent interpolation:(RNNInterpolationOptions)interpolation {
    return RNNInterpolate(from, to, precent, interpolation);
}

+ (CGRect)fromRect:(CGRect)from toRect:(CGRect)to precent:(CGFloat)p interpolation:(RNNInterpolationOptions)interpolation {
    return CGRectMake(RNNInterpolate(from.origin.x, to.origin.x, p, interpolation),
                      RNNInterpolate(from.origin.y, to.origin.y, p, interpolation),
                      RNNInterpolate(from.size.width, to.size.width, p, interpolation),
                      RNNInterpolate(from.size.height, to.size.height, p, interpolation));
}

static CGFloat RNNApplyInterpolation(CGFloat p, RNNInterpolationOptions interpolation) {
    switch (interpolation) {
        case RNNInterpolationAccelerate:
            return RNNAccelerate(p);
        case RNNInterpolationAccelerateDecelerate:
            return RNNAccelerateDecelerate(p);
        case RNNInterpolationLinear:
            return RNNLinear(p);
        case RNNInterpolationDecelerate:
            return RNNDecelerate(p);
    }
}

static CGFloat RNNInterpolate(CGFloat from, CGFloat to, CGFloat p, RNNInterpolationOptions interpolation) {
    return from + RNNApplyInterpolation(p, interpolation) * (to - from);
}

static CGFloat RNNLinear(CGFloat p) {
    return p;
}

static CGFloat RNNAccelerate(CGFloat p) {
    return p * p;
}

static CGFloat RNNDecelerate(CGFloat p) {
    return -(p * (p - 2));
}

static CGFloat RNNAccelerateDecelerate(CGFloat p) {
    if (p < 0.5) {
        return 4 * p * p * p;
    } else {
        CGFloat f = ((2 * p) - 2);
        return 0.5 * f * f * f + 1;
    }
}


@end
