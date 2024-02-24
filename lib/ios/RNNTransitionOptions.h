#import "RNNOptions.h"
#import "RNNTransitionDetailsOptions.h"

@interface RNNTransitionOptions : RNNOptions

@property(nonatomic, strong) RNNTransitionDetailsOptions *alpha;
@property(nonatomic, strong) RNNTransitionDetailsOptions *x;
@property(nonatomic, strong) RNNTransitionDetailsOptions *y;
@property(nonatomic, strong) RNNTransitionDetailsOptions *translationX;
@property(nonatomic, strong) RNNTransitionDetailsOptions *translationY;
@property(nonatomic, strong) RNNTransitionDetailsOptions *scaleX;
@property(nonatomic, strong) RNNTransitionDetailsOptions *scaleY;
@property(nonatomic, strong) RNNTransitionDetailsOptions *rotationX;
@property(nonatomic, strong) RNNTransitionDetailsOptions *rotationY;
@property(nonatomic, strong) RNNBool *waitForRender;
@property(nonatomic, strong) RNNBool *enable;

- (BOOL)shouldWaitForRender;
- (NSTimeInterval)maxDuration;
- (BOOL)hasAnimation;
- (BOOL)hasValue;

@end
