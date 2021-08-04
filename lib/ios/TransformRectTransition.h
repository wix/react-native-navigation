#import "RectTransition.h"

@interface TransformRectTransition : RectTransition

- (instancetype)initWithView:(UIView *)view
                        from:(CATransform3D)from
                          to:(CATransform3D)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@end
