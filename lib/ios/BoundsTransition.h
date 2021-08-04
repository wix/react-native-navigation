#import "ElementBaseTransition.h"

@interface BoundsTransition : ElementBaseTransition

@property(nonatomic) CGRect fromBounds;
@property(nonatomic) CGRect toBounds;

- (instancetype)initWithView:(UIView *)view
                        from:(CGRect)from
                          to:(CGRect)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@end
