#import "RNNBasePresenter.h"
#import "RNNRootViewCreator.h"
#import "RNNReactComponentRegistry.h"
#import "InteractivePopGestureDelegate.h"

@interface RNNNavigationControllerPresenter : RNNBasePresenter

@property (nonatomic, strong) InteractivePopGestureDelegate *interactivePopGestureDelegate;

- (instancetype)initWithcomponentRegistry:(RNNReactComponentRegistry *)componentRegistry;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
