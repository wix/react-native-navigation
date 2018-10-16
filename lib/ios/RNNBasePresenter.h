#import "RNNNavigationOptions.h"

@interface RNNBasePresenter : NSObject

@property (nonatomic, weak) id bindedViewController;

@property (nonatomic, retain) RNNNavigationOptions* defaultOptions;

- (void)bindViewController:(UIViewController *)bindedViewController;

- (void)applyOptions:(RNNNavigationOptions *)initialOptions;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options;

- (void)mergeOptions:(RNNNavigationOptions *)options;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
