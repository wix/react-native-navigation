#import "RNNBasePresenter.h"
#import "RNNNavigationButtons.h"
#import "RNNReactComponentRegistry.h"

@interface RNNViewControllerPresenter : RNNBasePresenter

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end
