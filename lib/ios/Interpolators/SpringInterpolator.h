//
//  SpringInterpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "Interpolator.h"

@interface SpringInterpolator : NSObject<Interpolator>

@property (readonly) CGFloat mass;
@property (readonly) CGFloat damping;
@property (readonly) CGFloat stiffness;
@property (readonly) CGFloat overshootClamping;
@property (readonly) CGFloat restDisplacementThreshold;
@property (readonly) CGFloat restSpeedThreshold;

- (instancetype)init:(CGFloat)mass damping:(CGFloat)damping stiffness:(CGFloat)stiffness;

@end
