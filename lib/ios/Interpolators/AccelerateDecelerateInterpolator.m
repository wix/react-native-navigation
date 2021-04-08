//
//  AccelerateDecelerateInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 06.10.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "AccelerateDecelerateInterpolator.h"

@implementation AccelerateDecelerateInterpolator

- (instancetype)init {
    self = [super init];
    return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
    if (progress < 0.5) {
        return 4 * progress * progress * progress;
    } else {
        CGFloat f = ((2 * progress) - 2);
        return 0.5 * f * f * f + 1;
    }
}

@end
