#import "RNNPushAnimation.h"

@implementation RNNPushAnimation

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition {
	self = [super init];
	self.screenTransition = screenTransition;
	
	return self;
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
	return 0.5;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
	UIViewController* toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
	
	[[transitionContext containerView] addSubview:toViewController.view];
	
	[self.screenTransition.content setupInitialTransitionForView:toViewController.view];
	[self.screenTransition.topBar setupInitialTransitionForView:toViewController.navigationController.navigationBar];
	
	
	[UIView animateWithDuration:[self transitionDuration:transitionContext] animations:^{
		[self.screenTransition.content completeTransitionForView:toViewController.view];
		[self.screenTransition.topBar completeTransitionForView:toViewController.navigationController.navigationBar];
	} completion:^(BOOL finished) {
		[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
	}];
}


@end
