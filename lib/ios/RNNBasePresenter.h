#import "RNNNavigationOptions.h"

@interface RNNBasePresenter : NSObject

@property (nonatomic, weak) id bindedViewController;

@property (nonatomic, retain) RNNNavigationOptions* defaultOptions;

- (void)bindViewController:(UIViewController *)bindedViewController;

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions;

- (void)applyOptions:(RNNNavigationOptions *)initialOptions;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options;

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

@end
