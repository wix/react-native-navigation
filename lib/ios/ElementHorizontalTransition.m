#import "ElementHorizontalTransition.h"

@implementation ElementHorizontalTransition

- (CGAffineTransform)animateWithProgress:(CGFloat)p {
    CGFloat x = [RNNInterpolator fromFloat:self.from toFloat:self.to precent:p interpolation:self.interpolation];    
    return CGAffineTransformMakeTranslation(x, 0);
}

- (CGFloat)initialValue {
    return self.view.frame.origin.x;
}

@end
