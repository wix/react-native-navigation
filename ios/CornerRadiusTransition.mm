//
//  CornerRadiusTransition.m
//  abseil
//
//  Created by Marc Rousavy on 02.09.20.
//

#import "CornerRadiusTransition.h"
#import "UIView+Utils.h"

@implementation CornerRadiusTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat toRadius = [RNNInterpolator fromFloat:self.from
                                          toFloat:self.to
                                          precent:p
                                     interpolator:self.interpolator];
    [self.view setCornerRadius:toRadius];
    return CATransform3DIdentity;
}

@end
