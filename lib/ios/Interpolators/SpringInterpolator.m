//
//  SpringInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "SpringInterpolator.h"
#import <QuartzCore/CAAnimation.h>

@implementation SpringInterpolator

- (instancetype)init:(CGFloat)mass
              damping:(CGFloat)damping
            stiffness:(CGFloat)stiffness
    allowsOverdamping:(BOOL)allowsOverdamping
      initialVelocity:(CGFloat)initialVelocity {
    self = [super init];
    if (self) {
        _mass = mass;
        _damping = damping;
        _stiffness = stiffness;
        _allowsOverdamping = allowsOverdamping;
        _velocity = initialVelocity;
    }
    return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
    // https://github.com/robb/RBBAnimation/blob/master/RBBAnimation/RBBSpringAnimation.m
    CGFloat b = _damping;
    CGFloat m = _mass;
    CGFloat k = _stiffness;
    CGFloat v0 = _velocity;

    NSParameterAssert(m > 0);
    NSParameterAssert(k > 0);
    NSParameterAssert(b > 0);

    CGFloat beta = b / (2 * m);
    CGFloat omega0 = sqrtf(k / m);
    CGFloat omega1 = sqrtf((omega0 * omega0) - (beta * beta));
    CGFloat omega2 = sqrtf((beta * beta) - (omega0 * omega0));

    CGFloat x0 = -1;

    if (!_allowsOverdamping && beta > omega0)
        beta = omega0;

    CGFloat t = progress;
    if (beta < omega0) {
        // Underdamped
        CGFloat envelope = expf(-beta * t);
        return -x0 +
               envelope * (x0 * cosf(omega1 * t) + ((beta * x0 + v0) / omega1) * sinf(omega1 * t));
    } else if (beta > omega0) {
        // Overdamped
        CGFloat envelope = expf(-beta * t);
        return -x0 + envelope *
                         (x0 * coshf(omega2 * t) + ((beta * x0 + v0) / omega2) * sinhf(omega2 * t));
    } else {
        // Critically damped
        CGFloat envelope = expf(-beta * t);
        return -x0 + envelope * (x0 + (beta * x0 + v0) * t);
    }
}

#pragma mark Private

- (CFTimeInterval)durationForEpsilon:(double)epsilon {
    CGFloat beta = _damping / (2 * _mass);

    CFTimeInterval duration = 0;
    while (expf(-beta * duration) >= epsilon) {
        duration += 0.1;
    }

    return duration;
}

@end
