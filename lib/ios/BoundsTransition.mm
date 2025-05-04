#import "BoundsTransition.h"

@implementation BoundsTransition

- (instancetype)initWithView:(UIView *)view
                        from:(CGRect)from
                          to:(CGRect)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator {
    self = [super initWithView:view
                    startDelay:startDelay
                      duration:duration
                  interpolator:interpolator];
    _fromBounds = from;
    _toBounds = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGRect toBounds = [RNNInterpolator fromRect:_fromBounds
                                         toRect:_toBounds
                                        precent:p
                                   interpolator:self.interpolator];
    self.view.bounds = toBounds;

    return CATransform3DIdentity;
}

@end
