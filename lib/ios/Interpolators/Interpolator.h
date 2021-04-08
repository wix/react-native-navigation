//
//  Interpolator.h
//  ReactNativeNavigation
//
//  Created by Marc Rousavy on 25.09.20.
//  Copyright © 2020 Wix. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <UIKit/UIKit.h>

@protocol Interpolator <NSObject>

/**
 * Interpolates the progress value on a custom interpolation function such as Ease/Bezier.
 * In Mathematical terms, the `progress` parameter is the `x` value of the interpolation function
 * `f(x)`, while the return value is `f` (or `y`)
 */
- (CGFloat)interpolate:(CGFloat)progress;

@end
