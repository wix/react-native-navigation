//
//  OvershootInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "OvershootInterpolator.h"

@implementation OvershootInterpolator

- (instancetype)init:(CGFloat)from to:(CGFloat)to tension:(CGFloat)tension
{
	self = [super init];
	if (self) {
		_from = from;
		_to = to;
		_tension = tension;
	}
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	return progress;
}

@end
