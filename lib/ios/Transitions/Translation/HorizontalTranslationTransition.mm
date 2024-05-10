#import "HorizontalTranslationTransition.h"

@implementation HorizontalTranslationTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat x = [RNNInterpolator fromFloat:self.from
                                   toFloat:self.to
                                   precent:p
                              interpolator:self.interpolator];
    return CATransform3DMakeTranslation(x, 0, 0);
}

- (CGFloat)initialValue {
    return self.view.frame.origin.x;
}

- (CGFloat)calculateFrom:(Double *)from {
    return from.hasValue ? from.get : 0;
}

- (CGFloat)calculateTo:(Double *)to {
    return to.hasValue ? to.get : 0;
}

@end
