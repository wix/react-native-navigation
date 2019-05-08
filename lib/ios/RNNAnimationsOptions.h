#import "RNNOptions.h"
#import "RNNScreenTransition.h"

@interface RNNAnimationsOptions : RNNOptions

@property (nonatomic, strong) RNNScreenTransition* push;
@property (nonatomic, strong) RNNScreenTransition* pop;
@property (nonatomic, strong) RNNScreenTransition* showModal;
@property (nonatomic, strong) RNNScreenTransition* dismissModal;
@property (nonatomic, strong) RNNScreenTransition* setStackRoot;
@property (nonatomic, strong) RNNScreenTransition* setRoot;

@end
