#import "RNNOptions.h"
#import "RNNTransitionStateHolder.h"
#import "RNNScreenTransition.h"

@interface RNNTransitionsOptions : RNNOptions

@property (nonatomic, strong) RNNScreenTransition* push;
@property (nonatomic, strong) RNNScreenTransition* pop;

@end
