#import "RNNOptions.h"
#import "RNNScreenTransition.h"
#import "RNNTransitionOptions.h"

@interface RNNAnimationsOptions : RNNOptions

@property(nonatomic, strong) RNNScreenTransition *push;
@property(nonatomic, strong) RNNScreenTransition *pop;
@property(nonatomic, strong) RNNEnterExitAnimation *showModal;
@property(nonatomic, strong) RNNEnterExitAnimation *dismissModal;
@property(nonatomic, strong) RNNScreenTransition *setStackRoot;
@property(nonatomic, strong) TransitionOptions *setRoot;

@end
