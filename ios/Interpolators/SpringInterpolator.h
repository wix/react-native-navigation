//
//  SpringInterpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import "Interpolator.h"
#import <Foundation/Foundation.h>

@interface SpringInterpolator : NSObject <Interpolator>

@property(readonly) CGFloat mass;
@property(readonly) CGFloat damping;
@property(readonly) CGFloat stiffness;
@property(readonly) BOOL allowsOverdamping;
@property(readonly) CGFloat velocity;

- (instancetype)init:(CGFloat)mass
              damping:(CGFloat)damping
            stiffness:(CGFloat)stiffness
    allowsOverdamping:(BOOL)allowsOverdamping
      initialVelocity:(CGFloat)initialVelocity;

@end
