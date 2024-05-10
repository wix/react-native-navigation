#import "DisplayLinkAnimation.h"
#import "Interpolator.h"
#import "RNNInterpolator.h"
#import "TransitionDetailsOptions.h"
#import <Foundation/Foundation.h>

#define DEGREES_TO_RADIANS(angle) ((angle) / 180.0 * M_PI)

@interface ElementBaseTransition : NSObject <DisplayLinkAnimation>

- (instancetype)initWithView:(UIView *)view
                  startDelay:(NSTimeInterval)startDelay
                    duration:(NSTimeInterval)duration
                interpolator:(id<Interpolator>)interpolator;

- (CGFloat)defaultDuration;

@property(readonly, strong) UIView *view;
@property(readonly, nonatomic) NSTimeInterval startDelay;
@property(readonly, nonatomic) NSTimeInterval duration;
@property(readonly, nonatomic) id<Interpolator> interpolator;

@end
