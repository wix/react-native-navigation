#import "RNNBasePresenter.h"
#import "RNNButtonsPresenter.h"
#import "RNNReactComponentRegistry.h"

@interface RNNComponentPresenter : RNNBasePresenter

- (void)renderComponents:(RNNNavigationOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@end
