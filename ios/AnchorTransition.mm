#import "AnchorTransition.h"
#import "RNNInterpolator.h"

@implementation AnchorTransition {
    CGPoint _initialPoint;
}

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
    _initialPoint = from;
    _from = from;
    _to = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat x = [RNNInterpolator fromFloat:self.from.x
                                   toFloat:self.to.x
                                   precent:p
                              interpolator:self.interpolator];
    CGFloat y = [RNNInterpolator fromFloat:self.from.y
                                   toFloat:self.to.y
                                   precent:p
                              interpolator:self.interpolator];
    return CATransform3DMakeTranslation(x - _initialPoint.x, y - _initialPoint.y, 0);
}

@end
