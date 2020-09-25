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

- (CGFloat)interpolate:(CGFloat)progress {
	return progress;
}

@end
