#import "TransitionDelegate.h"
#import "TransitionOptions.h"
#import "ContentTransitionCreator.h"

@interface ModalTransitionDelegate : TransitionDelegate

- (instancetype)initWithContentTransition:(TransitionOptions *)contentTransition uiManager:(RCTUIManager *)uiManager;

@property (nonatomic, strong) TransitionOptions* contentTransitionOptions;

@end
