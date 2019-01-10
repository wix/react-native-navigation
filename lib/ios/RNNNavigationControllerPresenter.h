#import "RNNBasePresenter.h"

@interface InteractivePopGestureDelegate : NSObject <UIGestureRecognizerDelegate>

@property(nonatomic, weak) UINavigationController *navigationController;
@property(nonatomic, weak) id<UIGestureRecognizerDelegate> originalDelegate;

@end

@interface RNNNavigationControllerPresenter : RNNBasePresenter

@property(nonatomic) InteractivePopGestureDelegate *interactivePopGestureDelegate;

- (void)applyOptionsBeforePopping:(RNNNavigationOptions *)options;

@end
