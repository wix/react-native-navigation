#import "PathTransition.h"

@implementation PathTransition

- (instancetype)initWithView:(UIView *)view
                    fromPath:(CGRect)fromPath
                      toPath:(CGRect)toPath
            fromCornerRadius:(CGFloat)fromCornerRadius
              toCornerRadius:(CGFloat)toCornerRadius
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator {
    self = [super initWithView:view
                          from:fromPath
                            to:toPath
                    startDelay:startDelay
                      duration:duration
                  interpolator:interpolator];
    self.fromCornerRadius = fromCornerRadius;
    self.toCornerRadius = toCornerRadius;

    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGRect toPath = [RNNInterpolator fromRect:self.from
                                       toRect:self.to
                                      precent:p
                                 interpolator:self.interpolator];
    CGFloat toRadius = [RNNInterpolator fromFloat:self.fromCornerRadius
                                          toFloat:self.toCornerRadius
                                          precent:p
                                     interpolator:self.interpolator];
    CAShapeLayer *mask = [CAShapeLayer new];
    mask.path = [UIBezierPath bezierPathWithRoundedRect:toPath cornerRadius:toRadius].CGPath;
    self.view.layer.mask = mask;
    return CATransform3DIdentity;
}

@end
