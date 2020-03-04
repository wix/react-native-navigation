#import "RNNBasePresenter.h"

@interface BottomTabPresenter : RNNBasePresenter

- (void)applyOptions:(RNNNavigationOptions *)options child:(UIViewController *)child;

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options  child:(UIViewController *)child;

- (void)createTabBarItem:(UIViewController *)child bottomTabOptions:(RNNBottomTabOptions *)bottomTabOptions;

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions child:(UIViewController *)child;

@end
