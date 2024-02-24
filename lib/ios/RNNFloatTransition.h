#import <Foundation/Foundation.h>

#import "RNNElementBaseTransition.h"
#import "RNNTransitionDetailsOptions.h"
#import "RNNDouble.h"

@interface RNNFloatTransition : RNNElementBaseTransition

- (instancetype)initWithView:(UIView *)view
           transitionDetails:(RNNTransitionDetailsOptions *)transitionDetails;

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
