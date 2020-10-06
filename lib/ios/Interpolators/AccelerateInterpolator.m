//
//  AccelerateInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 06.10.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "AccelerateInterpolator.h"

@implementation AccelerateInterpolator

- (instancetype)init
{
	self = [super init];
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	return progress * progress;
}

@end
