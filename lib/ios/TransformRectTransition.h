#import "ElementBaseTransition.h"
#import "RectTransition.h"

@interface TransformRectTransition : RectTransition

- (instancetype)initWithView:(UIView *)view fromRect:(CGRect)fromRect toRect:(CGRect)toRect fromAngle:(CGFloat)fromAngle toAngle:(CGFloat)toAngle startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation;

@property (nonatomic, readonly) CGFloat fromAngle;
@property (nonatomic, readonly) CGFloat toAngle;

@end
