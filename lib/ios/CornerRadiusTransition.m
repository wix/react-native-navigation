//
//  CornerRadiusTransition.m
//  abseil
//
//  Created by Marc Rousavy on 02.09.20.
//

#import "CornerRadiusTransition.h"

@implementation CornerRadiusTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat toRadius = [RNNInterpolator fromFloat:self.from toFloat:self.to precent:p interpolation:self.interpolation];
	self.view.layer.cornerRadius = toRadius;
    return CATransform3DIdentity;
}

@end
