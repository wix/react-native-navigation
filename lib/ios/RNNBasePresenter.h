#import "RNNNavigationOptions.h"

@interface RNNBasePresenter : NSObject

@property (nonatomic, weak) id bindedViewController;

- (void)bindViewController:(UIViewController *)bindedViewController;

- (void)applyOptions:(RNNNavigationOptions *)options;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options;

- (void)mergeOptions:(RNNNavigationOptions *)options;

@end
