#import "ElementBaseTransition.h"
#import "RectTransition.h"
#import "RNNViewLocation.h"

@interface TransformRectTransition : RectTransition

- (instancetype)initWithView:(UIView *)view
                viewLocation:(RNNViewLocation *)viewLocation
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

@end
