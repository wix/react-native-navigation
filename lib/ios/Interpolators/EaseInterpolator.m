//
//  EaseInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "EaseInterpolator.h"

static const CGFloat p0 = 0.42f;
static const CGFloat p1 = 0;
static const CGFloat p2 = 1;
static const CGFloat p3 = 1;

@implementation EaseInterpolator

- (instancetype)init
{
	self = [super init];
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	// ease = bezier(0.42, 0, 1, 1)
	
	if (progress < 0) {
		return 0;
	}
	if (progress > 1) {
		return 1;
	}
	
	// https://en.wikipedia.org/wiki/Bézier_curve#Cubic_Bézier_curves
	return powf(1 - progress, 3) * p0 + 3 * powf(1 - progress, 2) * progress * p1 + 3 * (1 - progress) * powf(progress, 2) * p2 + powf(progress, 3) * p3;
}

@end
