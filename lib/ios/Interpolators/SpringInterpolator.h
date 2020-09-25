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

@property (readonly) CGFloat from;
@property (readonly) CGFloat to;
@property (readonly) CGFloat mass;
@property (readonly) CGFloat damping;
@property (readonly) CGFloat stiffness;

- (instancetype)init:(CGFloat)from to:(CGFloat)to mass:(CGFloat)mass damping:(CGFloat)damping stiffness:(CGFloat)stiffness;

@end
