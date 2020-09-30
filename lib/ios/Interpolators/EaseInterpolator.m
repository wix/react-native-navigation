//
//  EaseInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "EaseInterpolator.h"

@implementation EaseInterpolator

- (instancetype)init
{
	self = [super init];
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	// https://easings.net/#easeOutCubic
    return 1 - powf(1 - progress, 3);
}

@end
