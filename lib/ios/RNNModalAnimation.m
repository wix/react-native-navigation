#import "RNNModalAnimation.h"

@implementation RNNModalAnimation

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition isDismiss:(BOOL)isDismiss {
	self = [super init];
	self.screenTransition = screenTransition;
	self.isDismiss = isDismiss;
	
	return self;
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
	return self.screenTransition.content.duration;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
	UIViewController* toViewController = [transitionContext viewControllerForKey:self.isDismiss ? UITransitionContextFromViewControllerKey : UITransitionContextToViewControllerKey];
	UIViewController* fromViewController = [transitionContext viewControllerForKey:self.isDismiss ? UITransitionContextToViewControllerKey : UITransitionContextFromViewControllerKey];
	
	if (self.isDismiss) {
		[[transitionContext containerView] addSubview:fromViewController.view];
		[[transitionContext containerView] addSubview:toViewController.view];
	} else {
		[[transitionContext containerView] addSubview:toViewController.view];
	}
	
	[_screenTransition.content setupInitialTransitionForView:toViewController.view];
	[_screenTransition.topBar setupInitialTransitionForView:toViewController.navigationController.navigationBar];
	
	
	[UIView animateWithDuration:[self transitionDuration:transitionContext] delay:self.screenTransition.content.startDelay options:self.screenTransition.content.interpolation animations:^{
		[_screenTransition.content completeTransitionForView:toViewController.view];
		[_screenTransition.topBar completeTransitionForView:toViewController.navigationController.navigationBar];
	} completion:^(BOOL finished) {
		[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
	}];
}


@end

