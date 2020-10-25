#import "ElementBaseTransition.h"
#import "RNNViewLocation.h"
#import "RectTransition.h"

@interface TransformRectTransition : RectTransition

- (instancetype)initWithView:(UIView *)view
                viewLocation:(RNNViewLocation *)viewLocation
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@end
