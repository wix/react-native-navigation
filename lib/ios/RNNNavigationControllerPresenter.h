#import "RNNBasePresenter.h"

@interface InteractivePopGestureDelegate : NSObject <UIGestureRecognizerDelegate>

@property(nonatomic, weak) UINavigationController *navigationController;
@property(nonatomic, weak) id<UIGestureRecognizerDelegate> originalDelegate;

@end

@interface RNNNavigationControllerPresenter : RNNBasePresenter

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

@end
