#import "RNNElementFrameTransition.h"

@implementation RNNElementFrameTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    self.view.frame = [RNNInterpolator fromRect:self.from
                                         toRect:self.to
                                        precent:p
                                   interpolator:self.interpolator];
    return CATransform3DIdentity;
}

@end
