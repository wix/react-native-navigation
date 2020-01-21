#import "VerticalTranslationTransition.h"

@implementation VerticalTranslationTransition

- (CGAffineTransform)animateWithProgress:(CGFloat)p {
    CGFloat y = [RNNInterpolator fromFloat:self.from toFloat:self.to precent:p interpolation:self.interpolation];
    return CGAffineTransformMakeTranslation(0, y);
}

- (CGFloat)initialValue {
    return self.view.frame.origin.y + self.from;
}

@end
