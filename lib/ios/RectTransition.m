#import "RectTransition.h"

@implementation RectTransition

- (instancetype)initWithView:(UIView *)view from:(CGRect)from to:(CGRect)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation {
    self = [super initWithView:view startDelay:startDelay duration:duration interpolation:interpolation];
    _from = from;
    _to = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGRect toFrame = [RNNInterpolator fromRect:self.from toRect:self.to precent:p interpolation:self.interpolation];
    self.view.frame = toFrame;
    return CATransform3DIdentity;
}

@end
