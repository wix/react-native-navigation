#import "ColorTransition.h"
#import "RNNInterpolator.h"

@implementation ColorTransition

- (instancetype)initWithView:(UIView *)view
                        from:(UIColor *)from
                          to:(UIColor *)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator {
    self = [super initWithView:view
                    startDelay:startDelay
                      duration:duration
                  interpolator:interpolator];
    _from = from;
    _to = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    self.view.backgroundColor = [RNNInterpolator fromColor:_from toColor:_to precent:p];
    return CATransform3DIdentity;
}

@end
