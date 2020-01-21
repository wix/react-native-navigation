#import "VerticalRotationTransition.h"
#define DEGREES_TO_RADIANS(angle) ((angle) / 180.0 * M_PI)

@implementation VerticalRotationTransition

- (CGAffineTransform)animateWithProgress:(CGFloat)p {
    double degrees = [RNNInterpolator fromFloat:self.from toFloat:self.to precent:p interpolation:self.interpolation];
    double rads = DEGREES_TO_RADIANS(degrees);
    
    return CGAffineTransformMakeRotation(rads);
}

- (CGFloat)initialValue {
	return 0;
}

@end
