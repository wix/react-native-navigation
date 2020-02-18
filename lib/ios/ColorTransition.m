#import "ColorTransition.h"
#import "RNNInterpolator.h"

@implementation ColorTransition

- (instancetype)initWithView:(UIView *)view from:(UIColor *)from to:(UIColor *)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation {
    self = [super initWithView:view startDelay:startDelay duration:duration interpolation:interpolation];
    _from = from;
    _to = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    self.view.backgroundColor = [RNNInterpolator fromColor:_from toColor:_to precent:p];
    return CATransform3DIdentity;
}

@end
