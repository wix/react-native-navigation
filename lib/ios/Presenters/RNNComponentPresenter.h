#import "RNNBasePresenter.h"
#import "RNNButtonsPresenter.h"
#import "RNNReactComponentRegistry.h"

@interface RNNComponentPresenter : RNNBasePresenter

- (void)renderComponents:(RNNNavigationOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions
                         buttonsPresenter:(RNNButtonsPresenter *)buttonsPresenter;

@end
