//
//  SpringInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "SpringInterpolator.h"

@implementation SpringInterpolator

- (instancetype)init:(CGFloat)from to:(CGFloat)to mass:(CGFloat)mass damping:(CGFloat)damping stiffness:(CGFloat)stiffness
{
	self = [super init];
	if (self) {
		_from = from;
		_to = to;
		_mass = mass;
		_damping = damping;
		_stiffness = stiffness;
	}
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	return progress;
}

@end
