#import "HorizontalTranslationTransition.h"

@implementation HorizontalTranslationTransition

- (CGAffineTransform)animateWithProgress:(CGFloat)p {
    CGFloat x = [RNNInterpolator fromFloat:self.from toFloat:self.to precent:p interpolation:self.interpolation];
    self.view.frame = CGRectMake(x, self.view.frame.origin.y, CGRectGetWidth(self.view.frame), CGRectGetHeight(self.view.frame));
    
    return CGAffineTransformIdentity;
}

- (CGFloat)initialValue {
    return self.view.frame.origin.x + self.from;
}

@end
