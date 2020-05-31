#import "VerticalRotationTransition.h"

@implementation VerticalRotationTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    double degrees = [RNNInterpolator fromFloat:self.from toFloat:self.to precent:p interpolation:self.interpolation];
    double rads = DEGREES_TO_RADIANS(degrees);
    return CATransform3DMakeRotation(rads, 0, 1, 0);
}

@end
