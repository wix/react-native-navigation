#import "RNNBasePresenter.h"
#import "RNNNavigationButtons.h"

@interface RNNViewControllerPresenter : RNNBasePresenter

@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

- (void)bindViewController:(UIViewController *)bindedViewController viewCreator:(id<RNNRootViewCreator>)creator;

@end
