#import "RNNOptions.h"
#import "RNNElementTransitionOptions.h"

@interface RNNScreenTransition : RNNOptions

@property (nonatomic, strong) RNNElementTransitionOptions* topBar;
@property (nonatomic, strong) RNNElementTransitionOptions* content;
@property (nonatomic, strong) RNNElementTransitionOptions* bottomTabs;

@property (nonatomic, strong) Bool* enable;
@property (nonatomic, strong) Bool* waitForRender;

- (BOOL)hasCustomAnimation;
- (double)maxDuration;

@end
