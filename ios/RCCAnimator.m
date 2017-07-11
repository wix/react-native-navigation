#import "RCCAnimator.h"

UIViewAnimationOptions const RCCNavigationTransitionCurve = 7 << 16;


@implementation UIView (TransitionShadow)
- (void)addLeftSideShadowWithFading
{
    CGFloat shadowWidth = 4.0f;
    CGFloat shadowVerticalPadding = -20.0f; // negative padding, so the shadow isn't rounded near the top and the bottom
    CGFloat shadowHeight = CGRectGetHeight(self.frame) - 2 * shadowVerticalPadding;
    CGRect shadowRect = CGRectMake(-shadowWidth, shadowVerticalPadding, shadowWidth, shadowHeight);
    UIBezierPath *shadowPath = [UIBezierPath bezierPathWithRect:shadowRect];
    self.layer.shadowPath = [shadowPath CGPath];
    self.layer.shadowOpacity = 0.2f;
    
    // fade shadow during transition
    CGFloat toValue = 0.0f;
    CABasicAnimation *animation = [CABasicAnimation animationWithKeyPath:@"shadowOpacity"];
    animation.fromValue = @(self.layer.shadowOpacity);
    animation.toValue = @(toValue);
    [self.layer addAnimation:animation forKey:nil];
    self.layer.shadowOpacity = toValue;
}
@end


@interface RCCAnimator()
@property (weak, nonatomic) UIViewController *toViewController;
@end

@implementation RCCAnimator

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext
{
    // Approximated lengths of the default animations.
    return [transitionContext isInteractive] ? 0.25f : 0.5f;
}

// Tries to animate a pop transition similarly to the default iOS' pop transition.
- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext
{
    UIViewController *toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
    UIViewController *fromViewController = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
    [[transitionContext containerView] insertSubview:toViewController.view belowSubview:fromViewController.view];
    
    // parallax effect; the offset matches the one used in the pop animation in iOS 7.1
    CGFloat toViewControllerXTranslation = - CGRectGetWidth([transitionContext containerView].bounds) * 0.3f;
    toViewController.view.bounds = [transitionContext containerView].bounds;
    toViewController.view.center = [transitionContext containerView].center;
    toViewController.view.transform = CGAffineTransformMakeTranslation(toViewControllerXTranslation, 0);
    
    // add a shadow on the left side of the frontmost view controller
    [fromViewController.view addLeftSideShadowWithFading];
    BOOL previousClipsToBounds = fromViewController.view.clipsToBounds;
    fromViewController.view.clipsToBounds = NO;
    
    // in the default transition the view controller below is a little dimmer than the frontmost one
    UIView *dimmingView = [[UIView alloc] initWithFrame:toViewController.view.bounds];
    dimmingView.backgroundColor = [UIColor colorWithWhite:0.0f alpha:0.1f];
    [toViewController.view addSubview:dimmingView];
    
    // Uses linear curve for an interactive transition, so the view follows the finger. Otherwise, uses a navigation transition curve.
    UIViewAnimationOptions curveOption = [transitionContext isInteractive] ? UIViewAnimationOptionCurveLinear : RCCNavigationTransitionCurve;
    
    [UIView animateWithDuration:[self transitionDuration:transitionContext] delay:0 options:UIViewAnimationOptionTransitionNone | curveOption animations:^{
        toViewController.view.transform = CGAffineTransformIdentity;
        fromViewController.view.transform = CGAffineTransformMakeTranslation(toViewController.view.frame.size.width, 0);
        dimmingView.alpha = 0.0f;
        
    } completion:^(BOOL finished) {
        [dimmingView removeFromSuperview];
        fromViewController.view.transform = CGAffineTransformIdentity;
        fromViewController.view.clipsToBounds = previousClipsToBounds;
        [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
    }];
    
    self.toViewController = toViewController;
}

- (void)animationEnded:(BOOL)transitionCompleted
{
    // restore the toViewController's transform if the animation was cancelled
    if (!transitionCompleted) {
        self.toViewController.view.transform = CGAffineTransformIdentity;
    }
}

@end
