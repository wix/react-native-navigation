//
//  BackInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "BackInterpolator.h"

static const CGFloat s = 1.70158;

@implementation BackInterpolator

- (instancetype)init
{
	self = [super init];
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	return progress * progress * ((s + 1) * progress - s);
}

@end
