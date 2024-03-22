#import "RNNElementBaseTransition.h"

@interface RNNCenterTransition : RNNElementBaseTransition

@property(nonatomic) CGPoint fromCenter;
@property(nonatomic) CGPoint toCenter;

- (instancetype)initWithView:(UIView *)view
                        from:(CGPoint)from
                          to:(CGPoint)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

@end
