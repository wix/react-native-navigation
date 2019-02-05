#import "RNNBasePresenter.h"
#import "RNNNavigationButtons.h"
#import "RNNReactComponentManager.h"

@interface RNNViewControllerPresenter : RNNBasePresenter

- (instancetype)initWithComponentManager:(RNNReactComponentManager *)componentManager;

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end
