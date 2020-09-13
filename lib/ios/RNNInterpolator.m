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
        case RNNInterpolationSpring:
			// TODO: Expose springiness and mass properties to JS-API and make uniform with Android implementation (only has tension property)
            return RNNSpring(p, 0.3, 3);
    }
}

static CGFloat RNNInterpolate(CGFloat from, CGFloat to, CGFloat p, RNNInterpolationOptions interpolation) {
    return from + RNNApplyInterpolation(p, interpolation) * (to - from);
}

static CGFloat RNNSpring(CGFloat p, CGFloat springiness, CGFloat mass) {
	// TODO: Cache those allocations
	CGFloat T0 = 1;
	CGFloat v0 = 0;
	CGFloat s0 = 1;

	CGFloat stiffness = mass * pow((2 * M_PI) / (T0), 2);
	CGFloat damping = 2 * (1 - springiness) * sqrt(stiffness * mass);

	CGFloat lambda = damping / (2 * mass);
	CGFloat w0 = sqrt(stiffness / mass);

	CGFloat wd = sqrt(fabs(pow(w0, 2) - pow(lambda, 2)));

	if (lambda < w0) {
		return fabs(1 - exp(-lambda * p) * (s0 * cos(wd * p) + ((v0 + s0 * lambda)/wd) * sin(wd * p)));
	} else if (lambda > w0) {
		return fabs(1 - exp(-lambda * p) * (((v0+s0 * (lambda + wd))/(2 * wd)) * exp(wd * p) + (s0 - (v0 + s0 * (lambda + wd)) / (2 * wd)) * exp(-wd * p)));
	} else {
		return fabs(1 - exp(-lambda * p) * (s0 + (v0 + lambda * s0) * p));
	}
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
