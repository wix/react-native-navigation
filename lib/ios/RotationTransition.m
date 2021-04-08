#import "RotationTransition.h"

@implementation RotationTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    double degrees = [RNNInterpolator fromFloat:self.from
                                        toFloat:self.to
                                        precent:p
                                   interpolator:self.interpolator];
    double rads = DEGREES_TO_RADIANS(degrees);
    return CATransform3DMakeRotation(rads, 0, 0, 1);
}

@end
