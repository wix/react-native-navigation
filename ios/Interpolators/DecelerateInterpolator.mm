//
//  DecelerateInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 06.10.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "DecelerateInterpolator.h"

@implementation DecelerateInterpolator

- (instancetype)init:(CGFloat)factor {
    self = [super init];
    if (self) {
        _factor = factor;
    }
    return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
    // https://easings.net/#easeOutCubic
    // Alternative: Ease: return 1 - powf(1 - progress, 3);

    if (_factor == 1.0f) {
        return (1.0f - (1.0f - progress) * (1.0f - progress));
    } else {
        return (1.0f - powf((1.0f - progress), 2 * _factor));
    }
}

@end
