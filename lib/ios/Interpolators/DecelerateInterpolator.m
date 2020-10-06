//
//  DecelerateInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 06.10.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "DecelerateInterpolator.h"

@implementation DecelerateInterpolator

- (instancetype)init
{
	self = [super init];
	return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
	// https://easings.net/#easeOutCubic
	// Alternative: Ease: return 1 - powf(1 - progress, 3);
	
	return -(progress * (progress - 2));
}

@end
