#import "RNNBasePresenter.h"
#import "InteractivePopGestureDelegate.h"

@interface RNNNavigationControllerPresenter : RNNBasePresenter

@property (nonatomic, strong) InteractivePopGestureDelegate *interactivePopGestureDelegate;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

@end
