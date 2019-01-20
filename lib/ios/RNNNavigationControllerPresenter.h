#import "RNNBasePresenter.h"
#import "RNNRootViewCreator.h"

@interface RNNNavigationControllerPresenter : RNNBasePresenter

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

- (void)bindViewController:(UIViewController *)bindedViewController viewCreator:(id<RNNRootViewCreator>)creator;

@end
