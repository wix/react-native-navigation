#import "RNNOptions.h"
#import "TransitionDetailsOptions.h"

@interface TransitionOptions : RNNOptions

@property(nonatomic, strong) TransitionDetailsOptions *alpha;
@property(nonatomic, strong) TransitionDetailsOptions *x;
@property(nonatomic, strong) TransitionDetailsOptions *y;
@property(nonatomic, strong) TransitionDetailsOptions *translationX;
@property(nonatomic, strong) TransitionDetailsOptions *translationY;
@property(nonatomic, strong) TransitionDetailsOptions *scaleX;
@property(nonatomic, strong) TransitionDetailsOptions *scaleY;
@property(nonatomic, strong) TransitionDetailsOptions *rotationX;
@property(nonatomic, strong) TransitionDetailsOptions *rotationY;
@property(nonatomic, strong) Bool *waitForRender;
@property(nonatomic, strong) Bool *enable;

- (BOOL)shouldWaitForRender;
- (NSTimeInterval)maxDuration;
- (BOOL)hasAnimation;
- (BOOL)hasValue;

@end
