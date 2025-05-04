#import "HorizontalScaleTransition.h"

@implementation HorizontalScaleTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat scaleX = [RNNInterpolator fromFloat:self.from
                                   toFloat:self.to
                                   precent:p
                              interpolator:self.interpolator];
    return CATransform3DMakeScale(scaleX, 1, 1);
}

- (CGFloat)calculateFrom:(Double *)from {
    return from.hasValue ? from.get : 1;
}

- (CGFloat)calculateTo:(Double *)to {
    return to.hasValue ? to.get : 1;
}

@end
