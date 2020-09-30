//
//  SpringInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "SpringInterpolator.h"

@implementation SpringInterpolator {
    CGFloat velocity;
    CGFloat current;
    CGFloat toValue;
    NSDate* lastTimeStamp;
}

- (instancetype)init:(CGFloat)mass damping:(CGFloat)damping stiffness:(CGFloat)stiffness
{
	self = [super init];
	if (self) {
		_mass = mass;
		_damping = damping;
		_stiffness = stiffness;
        // TODO: Make Configurable ?
        _overshootClamping = YES;
        _restSpeedThreshold = 10;
        _restDisplacementThreshold = 10;
        toValue = 1;
        velocity = 0;
        current = 0;
	}
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	// https://en.wikipedia.org/wiki/Harmonic_oscillator#Damped_harmonic_oscillator
    /*const { toValue, lastTimestamp, current, velocity } = animation;
    */
    NSTimeInterval diff = lastTimeStamp != nil ? [lastTimeStamp timeIntervalSinceNow] : 0;
    CGFloat deltaTime = MIN(diff, 64);
    lastTimeStamp = [NSDate date];
    
    CGFloat c = _damping;
    CGFloat m = _mass;
    CGFloat k = _stiffness;
    
    CGFloat v0 = velocity;
    CGFloat x0 = toValue - current;
    
    CGFloat zeta = c / (2 * sqrtf(k * m)); // damping ratio
    CGFloat omega0 = sqrtf(k / m); // undamped angular frequency of the oscillator (rad/ms)
    CGFloat omega1 = omega0 * sqrtf(1 - powf(zeta, 2)); // exponential decay
    
    CGFloat t = deltaTime > 0 ? deltaTime : 1;
    
    CGFloat sin1 = sinf(omega1 * t);
    CGFloat cos1 = cosf(omega1 * t);
    
    // under damped
    CGFloat underDampedEnvelope = expf(-zeta * omega0 * t);
    CGFloat underDampedFrag1 = underDampedEnvelope * (sin1 * ((v0 + zeta * omega0 * x0) / omega1) + x0 * cos1);
    
    CGFloat underDampedPosition = toValue - underDampedFrag1;
    // This looks crazy -- it's actually just the derivative of the oscillation function
    CGFloat underDampedVelocity = zeta * omega0 * underDampedFrag1 - underDampedEnvelope * (cos1 * (v0 + zeta * omega0 * x0) - omega1 * x0 * sin1);
    
    // critically damped
    CGFloat criticallyDampedEnvelope = expf(-omega0 * t);
    CGFloat criticallyDampedPosition = toValue - criticallyDampedEnvelope * (x0 + (v0 + omega0 * x0) * t);
    
    CGFloat criticallyDampedVelocity = criticallyDampedEnvelope * (v0 * (t * omega0 - 1) + t * x0 * omega0 * omega0);
    

    BOOL isVelocity = fabs(velocity) < _restSpeedThreshold;
    BOOL isDisplacement = _stiffness == 0 || fabs(toValue - current) < _restDisplacementThreshold;
    
    if (zeta < 1) {
        current = underDampedPosition;
        velocity = underDampedVelocity;
    } else {
        current = criticallyDampedPosition;
        velocity = criticallyDampedVelocity;
    }
    
    if (false || [self isOvershooting] || (isVelocity && isDisplacement)) {
        if (_stiffness != 0) {
            velocity = 0;
            current = toValue;
        }
    }

    return current;
}

#pragma mark Private

- (BOOL)isOvershooting {
    if (_overshootClamping && _stiffness != 0) {
        return current < toValue
            ? current > toValue
            : current < toValue;
    } else {
        return NO;
    }
};

@end
