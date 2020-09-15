#import "LayerBoundsTransition.h"

@implementation LayerBoundsTransition {
	CGRect _fromBounds;
    CGRect _toBounds;
}

- (instancetype)initWithView:(UIView *)view
						from:(CGRect)from
						  to:(CGRect)to
				  startDelay:(NSTimeInterval)startDelay
					duration:(NSTimeInterval)duration
			   interpolation:(Text *)interpolation {
    self = [super initWithView:view startDelay:startDelay duration:duration interpolation:interpolation];
    _fromBounds = from;
    _toBounds = to;
    return self;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    CGRect toBounds = [RNNInterpolator fromRect:_fromBounds toRect:_toBounds precent:p interpolation:self.interpolation];
    self.view.layer.bounds = toBounds;

    return CATransform3DIdentity;
}

@end
