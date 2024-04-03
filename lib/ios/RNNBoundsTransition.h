#import "RNNElementBaseTransition.h"

@interface RNNBoundsTransition : RNNElementBaseTransition

@property(nonatomic) CGRect fromBounds;
@property(nonatomic) CGRect toBounds;

- (instancetype)initWithView:(UIView *)view
                        from:(CGRect)from
                          to:(CGRect)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

@end
