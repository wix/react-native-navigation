#import "ElementBaseTransition.h"

@interface LayerBoundsTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view
						from:(CGRect)from
						  to:(CGRect)to
				  startDelay:(NSTimeInterval)startDelay
					duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@end
