#import "ScreenAnimationController.h"
#import "DisplayLinkAnimator.h"
#import "RNNScreenTransitionsCreator.h"
#import "SharedElementAnimator.h"
#import "UIViewController+LayoutProtocol.h"

@implementation ScreenAnimationController {
    RCTBridge *_bridge;
    id<UIViewControllerContextTransitioning> _transitionContext;
    SharedElementAnimator *_sharedElementAnimator;
    BOOL _animate;
    CGFloat _duration;
}

- (instancetype)initWithContentTransition:(RNNEnterExitAnimation *)contentTransition
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
    UINavigationController *toViewController =
        [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
    toViewController.view.alpha = 0;
    UIView *fromView = [transitionContext viewForKey:UITransitionContextFromViewKey];

    [transitionContext.containerView addSubview:fromView];
    [transitionContext.containerView addSubview:toViewController.view];
    [toViewController prepareForTransition];
}

- (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView {
    NSArray *transitions =
        [RNNScreenTransitionsCreator createTransitionsFromVC:fromVC
                                                        toVC:toVC
                                               containerView:containerView
                                           contentTransition:self.content
                                          elementTransitions:self.elementTransitions];
    _sharedElementAnimator =
        [[SharedElementAnimator alloc] initWithTransitions:self.sharedElementTransitions
                                                    fromVC:fromVC
                                                      toVC:toVC
                                             containerView:containerView];
    return [transitions arrayByAddingObjectsFromArray:[_sharedElementAnimator create]];
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

    [displayLinkAnimator setOnStart:^{
      [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey].view.alpha =
          1.f;
    }];

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

- (void)animationEnded:(BOOL)transitionCompleted {
    UIView *toView = [_transitionContext viewForKey:UITransitionContextToViewKey];
    UIView *fromView = [_transitionContext viewForKey:UITransitionContextFromViewKey];
    [_sharedElementAnimator animationEnded];
    toView.layer.transform = CATransform3DIdentity;
    fromView.layer.transform = CATransform3DIdentity;
    toView.alpha = 1.f;
    _transitionContext = nil;
    _sharedElementAnimator = nil;
}

@end
