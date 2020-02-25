#import "RNNBasePresenter.h"

@interface BottomTabPresenter : RNNBasePresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions;

- (void)applyDotIndicator:(UIViewController *)child;

@end
