/*
 *  Copyright (c) 2017-2018, Uber Technologies, Inc.
 *  Copyright (c) 2015-2018, Facebook, Inc.
 *
 *  This source code is licensed under the MIT license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

#import "UIImage+Snapshot.h"

@implementation UIImage (Snapshot)

+ (UIImage *)fb_imageForLayer:(CALayer *)layer {
    CGRect bounds = layer.bounds;
    NSAssert1(CGRectGetWidth(bounds), @"Zero width for layer %@", layer);
    NSAssert1(CGRectGetHeight(bounds), @"Zero height for layer %@", layer);

    UIGraphicsBeginImageContextWithOptions(bounds.size, NO, 0);
    CGContextRef context = UIGraphicsGetCurrentContext();
    NSAssert1(context, @"Could not generate context for layer %@", layer);
    CGContextSaveGState(context);
    [layer layoutIfNeeded];
    [layer renderInContext:context];
    CGContextRestoreGState(context);

    UIImage *snapshot = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return snapshot;
}

+ (UIImage *)fb_imageForViewLayer:(UIView *)view {
    [view layoutIfNeeded];
    return [self fb_imageForLayer:view.layer];
}

+ (UIImage *)fb_imageForView:(UIView *)view {
    // If the input view is already a UIWindow, then just use that. Otherwise wrap in a window.
    UIWindow *window = [view isKindOfClass:[UIWindow class]] ? (UIWindow *)view : view.window;
    BOOL removeFromSuperview = NO;
    if (!window) {
        window = [[UIApplication sharedApplication] keyWindow];
    }

    if (!view.window && view != window) {
        [window addSubview:view];
        removeFromSuperview = YES;
    }

    [view layoutIfNeeded];

    CGRect bounds = view.bounds;
    NSAssert1(CGRectGetWidth(bounds), @"Zero width for view %@", view);
    NSAssert1(CGRectGetHeight(bounds), @"Zero height for view %@", view);

    UIGraphicsImageRenderer *graphicsImageRenderer =
        [[UIGraphicsImageRenderer alloc] initWithSize:bounds.size];

    UIImage *snapshot = [graphicsImageRenderer
        imageWithActions:^(UIGraphicsImageRendererContext *_Nonnull rendererContext) {
          [view drawViewHierarchyInRect:bounds afterScreenUpdates:YES];
        }];

    if (removeFromSuperview) {
        [view removeFromSuperview];
    }

    return snapshot;
}

@end
