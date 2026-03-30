//
//  AccelerateInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 06.10.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "AccelerateInterpolator.h"

@implementation AccelerateInterpolator

- (instancetype)init:(CGFloat)factor {
    self = [super init];
    if (self) {
        _factor = factor;
    }
    return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
    if (_factor == 1.0f) {
        return progress * progress;
    } else {
        return powf(progress, _factor * 2);
    }
}

@end
