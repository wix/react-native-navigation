#import "ElementAlphaTransition.h"

@implementation ElementAlphaTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    self.view.alpha = [RNNInterpolator fromFloat:self.from
                                         toFloat:self.to
                                         precent:p
                                    interpolator:self.interpolator];
    return CATransform3DIdentity;
}

- (CGFloat)initialValue {
    return self.view.alpha;
}

@end
