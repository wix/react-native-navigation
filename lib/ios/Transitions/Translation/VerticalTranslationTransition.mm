#import "VerticalTranslationTransition.h"

@implementation VerticalTranslationTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat y = [RNNInterpolator fromFloat:self.from
                                   toFloat:self.to
                                   precent:p
                              interpolator:self.interpolator];
    return CATransform3DMakeTranslation(0, y, 0);
}

- (CGFloat)initialValue {
    return self.view.frame.origin.y;
}

- (CGFloat)calculateFrom:(Double *)from {
    return from.hasValue ? from.get : 0;
}

- (CGFloat)calculateTo:(Double *)to {
    return to.hasValue ? to.get : 0;
}

@end
