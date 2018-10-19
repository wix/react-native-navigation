#import "RNNBasePresenter.h"

@interface RNNBottomTabPresenter : NSObject

- (void)bindViewController:(UIViewController *)viewController;

- (void)applyOptions:(RNNNavigationOptions *)options;

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions;

@end
