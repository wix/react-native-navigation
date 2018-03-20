#import "RNNPushAnimation.h"

@implementation RNNPushAnimation

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition {
	self = [super init];
	self.screenTransition = screenTransition;
	
	return self;
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
	return self.screenTransition.content.duration;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
	UIViewController* toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
	
	[[transitionContext containerView] addSubview:toViewController.view];
	
	[self.screenTransition.content setupInitialTransitionForView:toViewController.view];
	[self.screenTransition.topBar setupInitialTransitionForView:toViewController.navigationController.navigationBar];
	[self.screenTransition.bottomTabs setupInitialTransitionForView:toViewController.tabBarController.tabBar];
	
	
	[UIView animateWithDuration:[self transitionDuration:transitionContext] delay:self.screenTransition.content.startDelay options:self.screenTransition.content.interpolation animations:^{
		[self.screenTransition.content completeTransitionForView:toViewController.view];
		[self.screenTransition.topBar completeTransitionForView:toViewController.navigationController.navigationBar];
		[self.screenTransition.bottomTabs completeTransitionForView:toViewController.tabBarController.tabBar];
	} completion:^(BOOL finished) {
		[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
	}];
}


@end
