#import "TransitionDelegate.h"
#import "ContentTransitionCreator.h"
#import "DisplayLinkAnimator.h"
#import "RNNScreenTransitionsCreator.h"

@implementation TransitionDelegate {
    RCTBridge *_bridge;
    id<UIViewControllerContextTransitioning> _transitionContext;
    BOOL _animate;
    CGFloat _duration;
}

- (instancetype)initWithContentTransition:(TransitionOptions *)contentTransition
                       elementTransitions:(NSArray<ElementTransitionOptions *> *)elementTransitions
                 sharedElementTransitions:
                     (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitions
                                 duration:(CGFloat)duration
                                   bridge:(RCTBridge *)bridge {
    self = [super init];
    _bridge = bridge;
    _content = contentTransition;
    _elementTransitions = elementTransitions;
    _sharedElementTransitions = sharedElementTransitions;
    _duration = duration;
    return self;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
    [_bridge.uiManager.observerCoordinator addObserver:self];
    _animate = YES;
    _transitionContext = transitionContext;
    [self prepareTransitionContext:transitionContext];

    UIViewController *fromVC =
        [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
    if (![fromVC.navigationController.childViewControllers containsObject:fromVC]) {
        [self performAnimationOnce];
    }
}

- (void)prepareTransitionContext:(id<UIViewControllerContextTransitioning>)transitionContext {
    UIView *toView = [transitionContext viewForKey:UITransitionContextToViewKey];
    [toView setNeedsLayout];
    [toView layoutIfNeeded];
    UIView *fromView = [transitionContext viewForKey:UITransitionContextFromViewKey];

    toView.alpha = 0;
    [transitionContext.containerView addSubview:fromView];
    [transitionContext.containerView addSubview:toView];
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
                                                       reversed:NO];
}

- (void)performAnimationOnce {
    if (_animate) {
        _animate = NO;
        RCTExecuteOnMainQueue(^{
          id<UIViewControllerContextTransitioning> transitionContext = self->_transitionContext;
          UIViewController *fromVC =
              [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
          UIViewController *toVC =
              [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
          NSArray *transitions = [self createTransitionsFromVC:fromVC
                                                          toVC:toVC
                                                 containerView:transitionContext.containerView];
          [self animateTransitions:transitions andTransitioningContext:transitionContext];
        });
    }
}

- (void)animateTransitions:(NSArray<id<DisplayLinkAnimatorDelegate>> *)animators
    andTransitioningContext:(id<UIViewControllerContextTransitioning>)transitionContext {
    DisplayLinkAnimator *displayLinkAnimator = [[DisplayLinkAnimator alloc]
        initWithDisplayLinkAnimators:animators
                            duration:[self transitionDuration:transitionContext]];

    [displayLinkAnimator setCompletion:^{
      if (![transitionContext transitionWasCancelled]) {
          [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
      }
    }];

    [displayLinkAnimator start];
}

- (NSTimeInterval)transitionDuration:(id<UIViewControllerContextTransitioning>)transitionContext {
    return _duration;
}

- (void)uiManagerDidPerformMounting:(RCTUIManager *)manager {
    [self performAnimationOnce];
}

- (id<UIViewControllerAnimatedTransitioning>)
    animationControllerForPresentedController:(UIViewController *)presented
                         presentingController:(UIViewController *)presenting
                             sourceController:(UIViewController *)source {
    return self;
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:
    (UIViewController *)dismissed {
    return self;
}

@end
