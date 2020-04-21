#import "FloatTransition.h"

@implementation FloatTransition

- (instancetype)initWithView:(UIView *)view transitionDetails:(TransitionDetailsOptions *)transitionDetails {
    self = [self initWithView:view
                         from:transitionDetails.from
                           to:transitionDetails.to
                   startDelay:[transitionDetails.startDelay getWithDefaultValue:0]
                     duration:[transitionDetails.duration getWithDefaultValue:[self defaultDuration]]
                interpolation:[transitionDetails.interpolation getWithDefaultValue:@"accelerateDecelerate"]];
    return self;
}

- (instancetype)initWithView:(UIView *)view fromFloat:(CGFloat)from toFloat:(CGFloat)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation {
    self = [super initWithView:view startDelay:startDelay duration:duration interpolation:interpolation];
    self.from = from;
    self.to = to;
    return self;
}

- (instancetype)initWithView:(UIView *)view from:(Double*)from to:(Double*)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation {
    self = [super initWithView:view startDelay:startDelay duration:duration interpolation:interpolation];
    _initialValue = self.initialValue;
    _from = [self calculateFrom:from];
    _to = [self calculateTo:to];
    return self;
}

- (CGFloat)calculateFrom:(Double*)from {
    return from.hasValue ? from.get : _initialValue;
}

- (CGFloat)calculateTo:(Double*)to {
    return to.hasValue ? to.get : _initialValue;
}

@end
