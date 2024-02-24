#import "RNNRectTransition.h"

@interface RNNTransformRectTransition : RNNRectTransition

- (instancetype)initWithView:(UIView *)view
                        from:(CATransform3D)from
                          to:(CATransform3D)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

@end
