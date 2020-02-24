#import "RNNOptions.h"
#import "ElementTransitionOptions.h"
#import "SharedElementTransitionOptions.h"

@interface RNNScreenTransition : RNNOptions

@property (nonatomic, strong) ElementTransitionOptions* topBar;
@property (nonatomic, strong) ElementTransitionOptions* content;
@property (nonatomic, strong) ElementTransitionOptions* bottomTabs;
@property (nonatomic, strong) NSArray<ElementTransitionOptions *>* elementTransitions;
@property (nonatomic, strong) NSArray<SharedElementTransitionOptions *>* sharedElementTransitions;

@property (nonatomic, strong) Bool* enable;
@property (nonatomic, strong) Bool* waitForRender;
@property (nonatomic, strong) TimeInterval* duration;

- (BOOL)hasCustomAnimation;
- (BOOL)shouldWaitForRender;
- (NSTimeInterval)maxDuration;


@end
