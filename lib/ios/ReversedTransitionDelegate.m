#import "ReversedTransitionDelegate.h"
#import "RNNScreenTransitionsCreator.h"

@implementation ReversedTransitionDelegate

- (void)prepareTransitionContext:(id<UIViewControllerContextTransitioning>)transitionContext {
    UIView *toView = [transitionContext viewForKey:UITransitionContextToViewKey];
    UIView *fromView = [transitionContext viewForKey:UITransitionContextFromViewKey];

    [transitionContext.containerView addSubview:toView];
    [transitionContext.containerView addSubview:fromView];
}

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView {
    return [RNNScreenTransitionsCreator createTransitionsFromVC:fromVC
                                                           toVC:toVC
                                                  containerView:containerView
                                              contentTransition:self.content
                                             elementTransitions:self.elementTransitions
                                       sharedElementTransitions:self.sharedElementTransitions
                                                       reversed:YES];
}

@end
