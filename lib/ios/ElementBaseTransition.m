#import "ElementBaseTransition.h"

@implementation ElementBaseTransition {
    Text* _interpolation;
}

@synthesize duration = _duration;
@synthesize startDelay = _startDelay;

- (instancetype)initWithView:(UIView *)view startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation {
    self = [super init];
    _view = view;
    _startDelay = startDelay;
    _duration = duration;
    _interpolation = interpolation;
    return self;
}

- (CGFloat)defaultDuration {
    return 300;
}

- (NSTimeInterval)startDelay {
    return _startDelay;
}

- (CGFloat)duration {
    return _duration;
}

- (CATransform3D)animateWithProgress:(CGFloat)p {
    return CATransform3DIdentity;
}

- (RNNInterpolationOptions)interpolation {
    return [RCTConvert RNNInterpolationOptions:_interpolation];
}

- (void)end { 

}

- (CGFloat)initialValue {
    return 0;
}

@end
