#import "RNNScreenReversedAnimationController.h"

@implementation RNNScreenReversedAnimationController

- (void)prepareTransitionContext:(id<UIViewControllerContextTransitioning>)transitionContext {
    UIView *toView = [transitionContext viewForKey:UITransitionContextToViewKey];
    toView.alpha = 0;
    UIView *fromView = [transitionContext viewForKey:UITransitionContextFromViewKey];
    [transitionContext.containerView addSubview:toView];
    [transitionContext.containerView addSubview:fromView];
    toView.alpha = 1;
}

@end
