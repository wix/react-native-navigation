#import "RNNBasePresenter.h"
#import "RNNButtonsPresenter.h"
#import "RNNReactComponentRegistry.h"

@interface RNNComponentPresenter : RNNBasePresenter

- (void)renderComponents:(RNNNavigationOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock;

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions
                         buttonsPresenter:(RNNButtonsPresenter *)buttonsPresenter;

- (void)applyTopBarButtonsBeforeShowing API_AVAILABLE(ios(26.0));

- (void)prepareTopBarPlatterForPushTransition API_AVAILABLE(ios(26.0));

- (void)attachTopBarTitleBeforePushUsingNavigationBar:(UINavigationBar *)navigationBar
    API_AVAILABLE(ios(26.0));

- (void)finishTopBarAfterPushTransition API_AVAILABLE(ios(26.0));

@end
