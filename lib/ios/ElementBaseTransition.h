#import "RNNDisplayLinkAnimation.h"
#import "RNNInterpolatorProtocol.h"
#import "RNNInterpolator.h"
#import "TransitionDetailsOptions.h"
#import <Foundation/Foundation.h>

#define DEGREES_TO_RADIANS(angle) ((angle) / 180.0 * M_PI)

@interface ElementBaseTransition : NSObject <RNNDisplayLinkAnimation>

- (instancetype)initWithView:(UIView *)view
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<RNNInterpolatorProtocol>)interpolator;

- (CGFloat)defaultDuration;

@property(readonly, strong) UIView *view;
@property(readonly, nonatomic) NSTimeInterval startDelay;
@property(readonly, nonatomic) NSTimeInterval duration;
@property(readonly, nonatomic) id<RNNInterpolatorProtocol> interpolator;

@end
