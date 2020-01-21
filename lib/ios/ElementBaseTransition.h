#import <Foundation/Foundation.h>
#import "TransitionDetailsOptions.h"
#import "DisplayLinkAnimation.h"
#import "RNNInterpolator.h"

@interface ElementBaseTransition : NSObject <DisplayLinkAnimation>

- (instancetype)initWithView:(UIView *)view transitionDetails:(TransitionDetailsOptions *)transitionDetails;

@property (readonly) CGFloat initialValue;
@property (readonly, strong) TransitionDetailsOptions *transitionDetails;
@property (readonly, strong) UIView* view;
@property (readonly, nonatomic) NSTimeInterval startDelay;
@property (readonly, nonatomic) NSTimeInterval duration;
@property (nonatomic) CGFloat from;
@property (nonatomic) CGFloat to;

@end
