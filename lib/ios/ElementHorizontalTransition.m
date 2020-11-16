#import "ElementHorizontalTransition.h"

@implementation ElementHorizontalTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat x = [RNNInterpolator fromFloat:self.from
                                   toFloat:self.to
                                   precent:p
                              interpolator:self.interpolator];
    return CATransform3DMakeTranslation(x - self.to, 0, 0);
}

- (CGFloat)initialValue {
    return self.view.frame.origin.x;
}

@end
