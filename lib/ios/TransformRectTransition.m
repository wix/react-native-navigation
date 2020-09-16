#import "TransformRectTransition.h"

@implementation TransformRectTransition {
    CATransform3D _fromTransform;
    CATransform3D _toTransform;
}

- (instancetype)initWithView:(UIView *)view
                viewLocation:(RNNViewLocation *)viewLocation
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
               interpolation:(Text *)interpolation {
    self = [super initWithView:view from:viewLocation.fromFrame to:viewLocation.toFrame startDelay:startDelay duration:duration interpolation:interpolation];
    _fromTransform = viewLocation.fromTransform;
    _toTransform = viewLocation.toTransform;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CATransform3D toTransform = [RNNInterpolator fromTransform:_fromTransform toTransform:_toTransform precent:p interpolation:self.interpolation];

    return toTransform;
}

@end
