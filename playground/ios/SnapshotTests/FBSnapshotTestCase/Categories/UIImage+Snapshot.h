/*
 *  Copyright (c) 2017-2018, Uber Technologies, Inc.
 *  Copyright (c) 2015-2018, Facebook, Inc.
 *
 *  This source code is licensed under the MIT license found in the
 *  LICENSE file in the root directory of this source tree.
 *
 */

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImage (Snapshot)

/// Uses renderInContext: to get a snapshot of the layer.
+ (nullable UIImage *)fb_imageForLayer:(CALayer *)layer;

/// Uses renderInContext: to get a snapshot of the view layer.
+ (nullable UIImage *)fb_imageForViewLayer:(UIView *)view;

/// Uses drawViewHierarchyInRect: to get a snapshot of the view and adds the view into a window if needed.
+ (nullable UIImage *)fb_imageForView:(UIView *)view;

@end

NS_ASSUME_NONNULL_END
