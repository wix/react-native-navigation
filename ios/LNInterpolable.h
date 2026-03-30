//
//  LNInterpolable.h
//
//  Created by Leo Natan on 01/10/2016.
//  Copyright © 2016 Leo Natan. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

NS_SWIFT_NAME(InterpolationBehavior)
typedef const NSString *LNInterpolationBehavior NS_TYPED_EXTENSIBLE_ENUM;

/**
 Interpolate using the default behavor of each implementation.
 */
extern LNInterpolationBehavior const LNInterpolationBehaviorUseDefault;

/**
 Classes implementing this protocol support interpolation.
 */
NS_SWIFT_NAME(Interpolable)
@protocol LNInterpolable <NSObject>

/**
 Interpolates between @c self and @c toValue accodring to @c progress using the
 default behavior.

 @param toValue The value to interpolate to
 @param progress The progress of the interpolation
 @return An object representing the interpolated value at the requested progress
 */
- (instancetype)interpolateToValue:(id)toValue
                          progress:(double)progress NS_SWIFT_NAME(interpolate(to:progress:));

/**
 Interpolates between @c self and @c toValue according to @c progress using @c
 behavior.

 @param toValue The value to interpolate to
 @param behavior The bahvior to use for interpolation
 @param progress The progress of the interpolation
 @return An object representing the interpolated value at the requested progress
 */
- (instancetype)interpolateToValue:(id)toValue
                          progress:(double)progress
                          behavior:(LNInterpolationBehavior)behavior
    NS_SWIFT_NAME(interpolate(to:progress:behavior:));

NS_ASSUME_NONNULL_END

@end
