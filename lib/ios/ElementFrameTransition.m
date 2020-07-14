#import "ElementFrameTransition.h"

@implementation ElementFrameTransition

- (CATransform3D)animateWithProgress:(CGFloat)p {
    self.view.frame = [RNNInterpolator fromRect:self.from toRect:self.to precent:p interpolation:self.interpolation];
    return CATransform3DIdentity;
}

@end
