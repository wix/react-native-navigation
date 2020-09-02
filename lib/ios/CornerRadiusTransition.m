//
//  CornerRadiusTransition.m
//  abseil
//
//  Created by Marc Rousavy on 02.09.20.
//

#import "CornerRadiusTransition.h"

@implementation CornerRadiusTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat toRadius = [RNNInterpolator from:self.from to:self.to precent:p interpolation:self.interpolation];
	self.view.layout.cornerRadius = toRadius;
    return CATransform3DIdentity;
}

@end
