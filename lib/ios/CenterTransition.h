#import "ElementBaseTransition.h"

@interface CenterTransition : ElementBaseTransition

@property(nonatomic) CGPoint fromCenter;
@property(nonatomic) CGPoint toCenter;

- (instancetype)initWithView:(UIView *)view
                        from:(CGPoint)from
                          to:(CGPoint)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@end
