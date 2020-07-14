#import "AnchorTransition.h"
#import "RNNInterpolator.h"

@implementation AnchorTransition {
    CGPoint _initialPoint;
}

- (instancetype)initWithView:(UIView *)view from:(CGPoint)from to:(CGPoint)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation {
    self = [super initWithView:view startDelay:startDelay duration:duration interpolation:interpolation];
    _initialPoint = from;
    _from = from;
    _to = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGFloat x = [RNNInterpolator fromFloat:self.from.x toFloat:self.to.x precent:p interpolation:self.interpolation];
    CGFloat y = [RNNInterpolator fromFloat:self.from.y toFloat:self.to.y precent:p interpolation:self.interpolation];
    return CATransform3DMakeTranslation(x - _initialPoint.x, y - _initialPoint.y, 0);
}

@end
