#import "RNNBasePresenter.h"
#import "RNNNavigationButtons.h"
#import "RNNReactComponentManager.h"

@interface RNNViewControllerPresenter : RNNBasePresenter

- (instancetype)initWithComponentManager:(RNNReactComponentManager *)componentManager;

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock;

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end
