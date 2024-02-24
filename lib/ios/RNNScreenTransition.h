#import "RNNElementTransitionOptions.h"
#import "RNNEnterExitAnimation.h"
#import "RNNOptions.h"
#import "RNNSharedElementTransitionOptions.h"

@interface RNNScreenTransition : RNNOptions

@property(nonatomic, strong) RNNElementTransitionOptions *topBar;
@property(nonatomic, strong) RNNEnterExitAnimation *content;
@property(nonatomic, strong) RNNElementTransitionOptions *bottomTabs;
@property(nonatomic, strong) NSArray<RNNElementTransitionOptions *> *elementTransitions;
@property(nonatomic, strong) NSArray<RNNSharedElementTransitionOptions *> *sharedElementTransitions;

@property(nonatomic, strong) RNNBool *enable;
@property(nonatomic, strong) RNNBool *waitForRender;
@property(nonatomic, strong) RNNTimeInterval *duration;

- (BOOL)hasCustomAnimation;
- (BOOL)shouldWaitForRender;
- (NSTimeInterval)maxDuration;

@end
