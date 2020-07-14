#import "ModalDismissTransitionDelegate.h"

@implementation ModalDismissTransitionDelegate

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
    return self;
}

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
    ContentTransitionCreator* contentTransition = [ContentTransitionCreator createTransition:self.contentTransitionOptions view:fromVC.view fromVC:fromVC toVC:toVC containerView:containerView];
    
    return @[contentTransition];
}

- (void)prepareTransitionContext:(id<UIViewControllerContextTransitioning>)transitionContext {
    UIView* toView = [transitionContext viewForKey:UITransitionContextToViewKey];
    UIView* fromView = [transitionContext viewForKey:UITransitionContextFromViewKey];
    
    fromView.alpha = 0;
    [transitionContext.containerView addSubview:toView];
    [transitionContext.containerView addSubview:fromView];
}

@end
