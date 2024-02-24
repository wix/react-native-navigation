#import "ElementBaseTransition.h"
#import <Foundation/Foundation.h>

@interface RNNFloatTransition : ElementBaseTransition

- (instancetype)initWithView:(UIView *)view
           transitionDetails:(TransitionDetailsOptions *)transitionDetails;

- (instancetype)initWithView:(UIView *)view
                   fromFloat:(CGFloat)from
                     toFloat:(CGFloat)to
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

- (CGFloat)calculateFrom:(RNNDouble *)from;

- (CGFloat)calculateTo:(RNNDouble *)to;

@property(readonly) CGFloat initialValue;
@property(nonatomic) CGFloat from;
@property(nonatomic) CGFloat to;

@end
