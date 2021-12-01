#import "TransformRectTransition.h"

@implementation TransformRectTransition {
    CATransform3D _fromTransform;
    CATransform3D _toTransform;
}

- (instancetype)initWithView:(UIView *)view
                        from:(CATransform3D)from
                          to:(CATransform3D)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator {
    self = [super initWithView:view
                    startDelay:startDelay
                      duration:duration
                  interpolator:interpolator];
    _fromTransform = from;
    _toTransform = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CATransform3D toTransform = [RNNInterpolator fromTransform:_fromTransform
                                                   toTransform:_toTransform
                                                       precent:p
                                                  interpolator:self.interpolator];
    return toTransform;
}

@end
