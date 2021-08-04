#import "ElementTransitionOptions.h"
#import "RNNEnterExitAnimation.h"
#import "SharedElementTransitionOptions.h"
#import "TransitionOptions.h"

@interface ViewAnimationOptions : RNNEnterExitAnimation

@property(nonatomic, strong) NSArray<ElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<SharedElementTransitionOptions *> *sharedElementTransitions;
@property(nonatomic, strong) Bool *waitForRender;

- (BOOL)shouldWaitForRender;
- (NSTimeInterval)maxDuration;
- (BOOL)hasAnimation;

@end
