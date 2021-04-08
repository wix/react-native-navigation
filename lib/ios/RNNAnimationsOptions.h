#import "RNNOptions.h"
#import "RNNScreenTransition.h"
#import "TransitionOptions.h"
#import "ViewAnimationOptions.h"

@interface RNNAnimationsOptions : RNNOptions

@property(nonatomic, strong) RNNScreenTransition *push;
@property(nonatomic, strong) RNNScreenTransition *pop;
@property(nonatomic, strong) ViewAnimationOptions *showModal;
@property(nonatomic, strong) ViewAnimationOptions *dismissModal;
@property(nonatomic, strong) RNNScreenTransition *setStackRoot;
@property(nonatomic, strong) TransitionOptions *setRoot;

@end
