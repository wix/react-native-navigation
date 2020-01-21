#import "ElementBaseTransition.h"

@implementation ElementBaseTransition

@synthesize duration = _duration;
@synthesize startDelay = _startDelay;
@synthesize initialValue = _initialValue;

- (instancetype)initWithView:(UIView *)view transitionDetails:(TransitionDetailsOptions *)transitionDetails {
    self = [super init];
    _view = view;
    _transitionDetails = transitionDetails;
    _startDelay = [transitionDetails.startDelay getWithDefaultValue:0];
    _duration = [transitionDetails.duration getWithDefaultValue:[self defaultDuration]];
    _initialValue = self.initialValue;
    _from = [self calculateFrom];
    _to = [self calculateTo];
    return self;
}

- (CGFloat)defaultDuration {
    return 300;
}

- (CGFloat)calculateFrom {
    return _transitionDetails.from.hasValue ? _transitionDetails.from.get : _initialValue;
}

- (CGFloat)calculateTo {
    return _transitionDetails.to.hasValue ? _transitionDetails.to.get : _initialValue;
}

- (NSTimeInterval)startDelay {
    return _startDelay;
}

- (CGFloat)duration {
    return _duration;
}

- (CGAffineTransform)animateWithProgress:(CGFloat)p {
    return CGAffineTransformIdentity;
}

- (RNNInterpolationOptions)interpolation {
    return [RCTConvert RNNInterpolationOptions:[_transitionDetails.interpolation getWithDefaultValue:@"accelerateDecelerate"]];
}

- (void)end { 

}

- (CGFloat)initialValue {
    return 0;
}

@end
