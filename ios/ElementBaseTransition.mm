#import "ElementBaseTransition.h"

@implementation ElementBaseTransition

@synthesize duration = _duration;
@synthesize startDelay = _startDelay;
@synthesize interpolator = _interpolator;

- (instancetype)initWithView:(UIView *)view
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator {
    self = [super init];
    _view = view;
    _startDelay = startDelay;
    _duration = duration;
    _interpolator = interpolator;
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

- (id<Interpolator>)interpolator {
    return _interpolator;
}

- (void)end {
}

- (CGFloat)initialValue {
    return 0;
}

@end
