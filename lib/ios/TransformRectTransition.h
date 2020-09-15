#import "ElementBaseTransition.h"
#import "RectTransition.h"
#import "RNNViewLocation.h"

@interface TransformRectTransition : RectTransition

- (instancetype)initWithView:(UIView *)view
                viewLocation:(RNNViewLocation *)viewLocation
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
               interpolation:(Text *)interpolation;

@property (nonatomic, readonly) CGFloat fromAngle;
@property (nonatomic, readonly) CGFloat toAngle;

@end
