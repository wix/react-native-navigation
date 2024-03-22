#import "RNNElementBaseTransition.h"

@interface RNNRectTransition : RNNElementBaseTransition

- (instancetype)initWithView:(UIView *)view
                        from:(CGRect)from
                          to:(CGRect)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

@property(nonatomic, readonly) CGRect from;
@property(nonatomic, readonly) CGRect to;

@end
