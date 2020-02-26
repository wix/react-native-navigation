#import "RNNBasePresenter.h"

@interface BottomTabPresenter : RNNBasePresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options  child:(UIViewController *)child;

- (void)updateChild:(UIViewController *)child bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions;

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions child:(UIViewController *)child;

@end
