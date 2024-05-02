//
//  Color+Interpolation.h
//
//  Created by Leo Natan on 01/10/2016.
//  Copyright © 2016 Leo Natan. All rights reserved.
//

#if __has_include(<UIKit/UIKit.h>) || __has_include(<AppKit/AppKit.h>)

#import "LNInterpolable.h"

#if __has_include(<UIKit/UIKit.h>)
#import <UIKit/UIKit.h>
#else
#import <AppKit/AppKit.h>
#endif

/**
 Interpolate using the LAB color space for optimal quality. This constant is
 equal to @c LNUseDefaultInterpolationBehavior.
 */
extern LNInterpolationBehavior const RNNInterpolationBehaviorUseLABColorSpace;

/**
 Interpolate using the RGB color space.
 */
extern LNInterpolationBehavior const RNNInterpolationBehaviorUseRGBColorSpace;

/**
 Interpolates between colors.

 By default, colors are interpolated in the Lab color space for optimal quality
 at the expense of some performance. Use @c LNUseRGBInterpolationBehavior for
 better performance but suboptimal quality.
 */
#if __has_include(<UIKit/UIKit.h>)
@interface UIColor (LNInterpolation) <LNInterpolable>
@end
#else
@interface NSColor (LNInterpolation) <LNInterpolable>
@end
#endif

#endif
