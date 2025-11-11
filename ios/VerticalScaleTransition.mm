#import "VerticalScaleTransition.h"

@implementation VerticalScaleTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat scaleY = [RNNInterpolator fromFloat:self.from
                                   toFloat:self.to
                                   precent:p
                              interpolator:self.interpolator];
    return CATransform3DMakeScale(1, scaleY, 1);
}

- (CGFloat)calculateFrom:(Double *)from {
    return from.hasValue ? from.get : 1;
}

- (CGFloat)calculateTo:(Double *)to {
    return to.hasValue ? to.get : 1;
}

@end
