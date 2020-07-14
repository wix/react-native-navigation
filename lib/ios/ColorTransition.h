#import "FloatTransition.h"
#import "ElementBaseTransition.h"

@interface ColorTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view from:(UIColor *)from to:(UIColor *)to startDelay:(NSTimeInterval)startDelay duration:(NSTimeInterval)duration interpolation:(Text *)interpolation;

@property (nonatomic, readonly, strong) UIColor* from;
@property (nonatomic, readonly, strong) UIColor* to;


@end
