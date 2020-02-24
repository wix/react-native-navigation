#import "ElementBaseTransition.h"

@interface AnchorTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view from:(CGPoint)from to:(CGPoint)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation;

@property (nonatomic, readonly) CGPoint from;
@property (nonatomic, readonly) CGPoint to;

@end
