#import "RNNBasePresenter.h"

@interface RNNNavigationControllerPresenter : RNNBasePresenter

@property(nonatomic) InteractivePopGestureDelegate *interactivePopGestureDelegate;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

@end
