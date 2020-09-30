#import "TransformRectTransition.h"

@implementation TransformRectTransition {
    CATransform3D _fromTransform;
    CATransform3D _toTransform;
    CGRect _fromBounds;
    CGRect _toBounds;
}

- (instancetype)initWithView:(UIView *)view
                viewLocation:(RNNViewLocation *)viewLocation
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator {
    self = [super initWithView:view from:viewLocation.fromFrame to:viewLocation.toFrame startDelay:startDelay duration:duration interpolator:interpolator];
    _fromAngle = viewLocation.fromAngle;
    _toAngle = viewLocation.toAngle;
    _fromTransform = viewLocation.fromTransform;
    _toTransform = viewLocation.toTransform;
    _fromBounds = viewLocation.fromBounds;
    _toBounds = viewLocation.toBounds;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGRect toBounds = [RNNInterpolator fromRect:_fromBounds toRect:_toBounds precent:p interpolator:self.interpolator];
    CGRect toFrame = [RNNInterpolator fromRect:self.from toRect:self.to precent:p interpolator:self.interpolator];

    CATransform3D toTransform = CATransform3DIdentity;

    toTransform.m11 = [RNNInterpolator fromFloat:_fromTransform.m11 toFloat:_toTransform.m11 precent:p interpolator:self.interpolator];
    toTransform.m12 = [RNNInterpolator fromFloat:_fromTransform.m12 toFloat:_toTransform.m12 precent:p interpolator:self.interpolator];
    toTransform.m13 = [RNNInterpolator fromFloat:_fromTransform.m13 toFloat:_toTransform.m13 precent:p interpolator:self.interpolator];
    toTransform.m14 = [RNNInterpolator fromFloat:_fromTransform.m14 toFloat:_toTransform.m14 precent:p interpolator:self.interpolator];

    toTransform.m21 = [RNNInterpolator fromFloat:_fromTransform.m21 toFloat:_toTransform.m21 precent:p interpolator:self.interpolator];
    toTransform.m22 = [RNNInterpolator fromFloat:_fromTransform.m22 toFloat:_toTransform.m22 precent:p interpolator:self.interpolator];
    toTransform.m23 = [RNNInterpolator fromFloat:_fromTransform.m23 toFloat:_toTransform.m23 precent:p interpolator:self.interpolator];
    toTransform.m24 = [RNNInterpolator fromFloat:_fromTransform.m24 toFloat:_toTransform.m24 precent:p interpolator:self.interpolator];

    toTransform.m31 = [RNNInterpolator fromFloat:_fromTransform.m31 toFloat:_toTransform.m31 precent:p interpolator:self.interpolator];
    toTransform.m32 = [RNNInterpolator fromFloat:_fromTransform.m32 toFloat:_toTransform.m32 precent:p interpolator:self.interpolator];
    toTransform.m33 = [RNNInterpolator fromFloat:_fromTransform.m33 toFloat:_toTransform.m33 precent:p interpolator:self.interpolator];
    toTransform.m34 = [RNNInterpolator fromFloat:_fromTransform.m34 toFloat:_toTransform.m34 precent:p interpolator:self.interpolator];

    toTransform.m41 = [RNNInterpolator fromFloat:_fromTransform.m41 toFloat:_toTransform.m41 precent:p interpolator:self.interpolator];
    toTransform.m42 = [RNNInterpolator fromFloat:_fromTransform.m42 toFloat:_toTransform.m42 precent:p interpolator:self.interpolator];
    toTransform.m43 = [RNNInterpolator fromFloat:_fromTransform.m43 toFloat:_toTransform.m43 precent:p interpolator:self.interpolator];
    toTransform.m44 = [RNNInterpolator fromFloat:_fromTransform.m44 toFloat:_toTransform.m44 precent:p interpolator:self.interpolator];

    self.view.frame = toFrame;
    self.view.layer.bounds = toBounds;

    return toTransform;
}

@end
