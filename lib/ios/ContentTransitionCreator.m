#import "ContentTransitionCreator.h"

@implementation ContentTransitionCreator

- (instancetype)initWithTransitionOptions:(ElementTransitionOptions *)transitionOptions fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView view:(UIView *)view {
    self = [self initWithTransitionOptions:transitionOptions fromVC:fromVC toVC:toVC containerView:containerView];
    self.view = view;
    if (!transitionOptions.alpha.hasAnimation) {
        transitionOptions.alpha = self.defaultAlphaTransition;
    }
    self.animations = [self createAnimations];
    return self;
}

- (TransitionDetailsOptions *)defaultAlphaTransition {
    TransitionDetailsOptions* defaultAlphaTransition = [TransitionDetailsOptions new];
    defaultAlphaTransition.duration = [TimeInterval withValue:300];
    defaultAlphaTransition.from = [Double withValue:0];
    defaultAlphaTransition.to = [Double withValue:1];
    return defaultAlphaTransition;
}

@end
