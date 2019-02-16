#import "RNNBasePresenter.h"
#import "RNNRootViewCreator.h"
#import "RNNReactComponentRegistry.h"

@interface RNNNavigationControllerPresenter : RNNBasePresenter

- (instancetype)initWithcomponentRegistry:(RNNReactComponentRegistry *)componentRegistry;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
