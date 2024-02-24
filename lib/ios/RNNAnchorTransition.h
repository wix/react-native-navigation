#import "RNNElementBaseTransition.h"
#import "RNNInterpolatorProtocol.h"

@interface RNNAnchorTransition : RNNElementBaseTransition

- (instancetype)initWithView:(UIView *)view
                        from:(CGPoint)from
                          to:(CGPoint)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

@property(nonatomic, readonly) CGPoint from;
@property(nonatomic, readonly) CGPoint to;

@end
