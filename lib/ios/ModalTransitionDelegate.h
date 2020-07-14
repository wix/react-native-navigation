#import "TransitionDelegate.h"
#import "TransitionOptions.h"
#import "ContentTransitionCreator.h"

@interface ModalTransitionDelegate : TransitionDelegate

- (instancetype)initWithContentTransition:(TransitionOptions *)contentTransition bridge:(RCTBridge *)bridge;

@property (nonatomic, strong) TransitionOptions* contentTransitionOptions;

@end
