#import "RNNFloatTransition.h"

@implementation RNNFloatTransition

- (instancetype)initWithView:(UIView *)view
           transitionDetails:(TransitionDetailsOptions *)transitionDetails {
    self = [self initWithView:view
                         from:transitionDetails.from
                           to:transitionDetails.to
                   startDelay:[transitionDetails.startDelay withDefault:0]
                     duration:[transitionDetails.duration withDefault:[self defaultDuration]]
                 interpolator:transitionDetails.interpolator];
    return self;
}

- (instancetype)initWithView:(UIView *)view
                   fromFloat:(CGFloat)from
                     toFloat:(CGFloat)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator {
    self = [super initWithView:view
                    startDelay:startDelay
                      duration:duration
                  interpolator:interpolator];
    self.from = from;
    self.to = to;
    return self;
}

- (instancetype)initWithView:(UIView *)view
                        from:(RNNDouble *)from
                          to:(RNNDouble *)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator {
    self = [super initWithView:view
                    startDelay:startDelay
                      duration:duration
                  interpolator:interpolator];
    _initialValue = self.initialValue;
    _from = [self calculateFrom:from];
    _to = [self calculateTo:to];
    return self;
}

- (CGFloat)calculateFrom:(RNNDouble *)from {
    return from.hasValue ? from.get : _initialValue;
}

- (CGFloat)calculateTo:(RNNDouble *)to {
    return to.hasValue ? to.get : _initialValue;
}

@end
