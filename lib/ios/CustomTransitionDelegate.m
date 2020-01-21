#import "CustomTransitionDelegate.h"
#import "SharedElementTransitionsCreator.h"
#import "ScreenTransitionsCreator.h"
#import "DisplayLinkAnimator.h"

@implementation CustomTransitionDelegate {
    ScreenTransitionsCreator* _transitionsCreator;
    RCTUIManager* _uiManager;
    UIViewController* _fromVC;
    UIViewController* _toVC;
    id <UIViewControllerContextTransitioning> _transitionContext;
    BOOL _animate;
}

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition uiManager:(RCTUIManager *)uiManager {
    self = [super init];
    _transitionsCreator = [[ScreenTransitionsCreator alloc] initWithScreenTransition:screenTransition];
    _uiManager = uiManager;
    [_uiManager.observerCoordinator addObserver:self];
    return self;
}

- (void)animateTransitions:(NSArray<id<DisplayLinkAnimatorDelegate>>*)animators andTransitioningContext:(id<UIViewControllerContextTransitioning>)transitionContext {
    DisplayLinkAnimator* displayLinkAnimator = [[DisplayLinkAnimator alloc] initWithDisplayLinkAnimators:animators duration:[self transitionDuration:transitionContext]];
    
    [displayLinkAnimator setCompletion:^{
        if (![transitionContext transitionWasCancelled]) {
            [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
        }
    }];
    
    [displayLinkAnimator start];
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
    return _transitionsCreator.minDuration;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
    _animate = YES;
    UIViewController* fromVC = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
    UIViewController* toVC = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
    _toVC = toVC;
    _fromVC = fromVC;
    _transitionContext = transitionContext;
    [_transitionContext.containerView addSubview:_toVC.view];
    _toVC.view.alpha = 0;
    
    if (![fromVC.navigationController.childViewControllers containsObject:fromVC]) {
        [self performAnimationOnce];
    }
}

- (void)performAnimationOnce {
    if (_animate) {
        _animate = NO;
        RCTExecuteOnMainQueue(^{
            id<UIViewControllerContextTransitioning> transitionContext = self->_transitionContext;
            UIViewController* fromVC = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
            UIViewController* toVC = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
            NSArray* transitions = [self->_transitionsCreator createFromVC:fromVC toVC:toVC containerView:transitionContext.containerView];
            [self animateTransitions:transitions andTransitioningContext:transitionContext];
        });
    }
}

- (void)uiManagerDidPerformMounting:(RCTUIManager *)manager {
    [self performAnimationOnce];
}

@end
