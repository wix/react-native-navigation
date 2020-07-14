#import "HorizontalTranslationTransition.h"

@implementation HorizontalTranslationTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat x = [RNNInterpolator fromFloat:self.from + self.to toFloat:self.to precent:p interpolation:self.interpolation];
    return CATransform3DMakeTranslation(x - self.to, 0, 0);
}

- (CGFloat)initialValue {
    return self.view.frame.origin.x;
}

@end
