//
//  EaseInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "EaseInterpolator.h"

@implementation EaseInterpolator

- (instancetype)init:(CGFloat)from to:(CGFloat)to
{
	self = [super init];
	if (self) {
		_from = from;
		_to = to;
	}
	return self;
}

static CGFloat p0 = 0.42f;
static CGFloat p1 = 0;
static CGFloat p2 = 1;
static CGFloat p3 = 1;

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
