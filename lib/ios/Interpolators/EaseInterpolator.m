//
//  EaseInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "EaseInterpolator.h"

@implementation EaseInterpolator

- (instancetype)init:(float)from to:(float)to
{
	self = [super init];
	if (self) {
		_from = from;
		_to = to;
	}
	return self;
}

- (float)interpolate:(float)progress {
	return _from;
}

@end
