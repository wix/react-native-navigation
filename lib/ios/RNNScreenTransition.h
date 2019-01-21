#import "RNNOptions.h"
#import "RNNTransitionStateHolder.h"

@interface RNNScreenTransition : RNNOptions

@property (nonatomic, strong) RNNTransitionStateHolder* topBar;
@property (nonatomic, strong) RNNTransitionStateHolder* content;
@property (nonatomic, strong) RNNTransitionStateHolder* bottomTabs;

@property (nonatomic, strong) Bool* enable;
@property (nonatomic, strong) Bool* waitForRender;

- (BOOL)hasCustomAnimation;

@end
