#import "ElementVerticalTransition.h"

@implementation ElementVerticalTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat y = [RNNInterpolator fromFloat:self.from toFloat:self.to precent:p interpolation:self.interpolation];
    return CATransform3DMakeTranslation(0, y - self.to, 0);
}

- (CGFloat)initialValue {
    return self.view.frame.origin.y;
}

@end
