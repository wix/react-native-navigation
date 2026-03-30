//
//  OvershootInterpolator.m
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "OvershootInterpolator.h"

@implementation OvershootInterpolator

- (instancetype)init:(CGFloat)tension {
    self = [super init];
    if (self) {
        _tension = tension;
    }
    return self;
}

- (CGFloat)interpolate:(CGFloat)progress {
    // _o(t) = t * t * ((tension + 1) * t + tension)
    // o(t) = _o(t - 1) + 1
    CGFloat t = progress - 1;
    CGFloat _ot = t * t * ((_tension + 1) * t + _tension) + 1.0f;
    return _ot;
}

@end
