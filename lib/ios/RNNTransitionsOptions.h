#import "RNNOptions.h"
#import "RNNTransitionStateHolder.h"
#import "RNNScreenTransition.h"

@interface RNNTransitionsOptions : RNNOptions

@property (nonatomic, strong) RNNScreenTransition* push;
@property (nonatomic, strong) RNNScreenTransition* pop;
@property (nonatomic, strong) RNNScreenTransition* showModal;
@property (nonatomic, strong) RNNScreenTransition* dismissModal;
@property (nonatomic, strong) RNNScreenTransition* setStackRoot;

@end
