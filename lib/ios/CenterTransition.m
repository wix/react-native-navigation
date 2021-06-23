#import "CenterTransition.h"

@implementation CenterTransition

- (instancetype)initWithView:(UIView *)view
                        from:(CGPoint)from
                          to:(CGPoint)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator {
    self = [super initWithView:view
                    startDelay:startDelay
                      duration:duration
                  interpolator:interpolator];
    self.fromCenter = from;
    self.toCenter = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGPoint toCenter = [RNNInterpolator fromPoint:self.fromCenter
                                          toPoint:self.toCenter
                                          precent:p
                                     interpolator:self.interpolator];
    self.view.center = toCenter;
    return CATransform3DIdentity;
}

@end
