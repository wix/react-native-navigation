#import "RectTransition.h"

@implementation RectTransition

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
    _from = from;
    _to = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGRect toFrame = [RNNInterpolator fromRect:self.from
                                        toRect:self.to
                                       precent:p
                                  interpolator:self.interpolator];
    self.view.frame = toFrame;
    return CATransform3DIdentity;
}

@end
