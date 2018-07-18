#import "RNNOptions.h"
#import "RNNTransitionStateHolder.h"

@interface RNNScreenTransition : RNNOptions

@property (nonatomic, strong) RNNTransitionStateHolder* topBar;
@property (nonatomic, strong) RNNTransitionStateHolder* content;
@property (nonatomic, strong) RNNTransitionStateHolder* bottomTabs;

@property (nonatomic) BOOL enable;
@property (nonatomic) BOOL waitForRender;

- (BOOL)hasCustomAnimation;

@end
