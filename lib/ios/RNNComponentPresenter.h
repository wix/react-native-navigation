#import "RNNBasePresenter.h"
#import "RNNNavigationButtons.h"
#import "RNNReactComponentRegistry.h"

@interface RNNComponentPresenter : RNNBasePresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry:(RNNNavigationOptions *)defaultOptions;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end
