#import "RNNBasePresenter.h"
#import "InteractivePopGestureDelegate.h"

@interface RNNNavigationControllerPresenter : RNNBasePresenter

@property(nonatomic) InteractivePopGestureDelegate *interactivePopGestureDelegate;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

@end
