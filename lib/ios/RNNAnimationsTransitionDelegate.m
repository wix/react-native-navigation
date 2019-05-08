#import "RNNAnimationsTransitionDelegate.h"

@implementation RNNAnimationsTransitionDelegate

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition isDismiss:(BOOL)isDismiss {
	self = [super init];
	self.screenTransition = screenTransition;
	self.isDismiss = isDismiss;
	return self;
}

- (nullable id <UIViewControllerAnimatedTransitioning>)animationControllerForPresentedController:(UIViewController *)presented presentingController:(UIViewController *)presenting sourceController:(UIViewController *)source {
	return self;
}

- (id<UIViewControllerAnimatedTransitioning>)animationControllerForDismissedController:(UIViewController *)dismissed {
	return self;
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
	return self.screenTransition.maxDuration / 1000;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
	UIViewController* toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
	UIViewController* fromViewController = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
	}];
	
	if (_isDismiss) {
		[[transitionContext containerView] addSubview:toViewController.view];
		[[transitionContext containerView] addSubview:fromViewController.view];
		[self animateElement:self.screenTransition.content view:fromViewController.view elementName:@"content"];
	} else {
		[[transitionContext containerView] addSubview:toViewController.view];
		[self animateElement:self.screenTransition.content view:toViewController.view elementName:@"content"];
	}
	
	[CATransaction commit];
}

- (void)animationWithKeyPath:(NSString *)keyPath from:(id)from to:(id)to duration:(CFTimeInterval)duration forView:(UIView *)view animationName:(NSString *)animationName {
	CABasicAnimation *animation = [CABasicAnimation animation];
	animation.keyPath = keyPath;
	animation.fromValue = from;
	animation.toValue = to;
	animation.duration = duration / 1000;
	[view.layer addAnimation:animation forKey:animationName];
}

- (void)animateElement:(RNNElementTransitionOptions *)element view:(UIView *)view elementName:(NSString *)elementName {
	[self animationWithKeyPath:@"position.x" from:@(view.layer.position.x + [element.x.from getWithDefaultValue:0]) to:@(view.layer.position.x + [element.x.to getWithDefaultValue:0]) duration:[element.x.duration getWithDefaultValue:1] forView:view animationName:@"element.position.x"];
	[self animationWithKeyPath:@"position.y" from:@(view.layer.position.y + [element.y.from getWithDefaultValue:0]) to:@(view.layer.position.y + [element.y.to getWithDefaultValue:0]) duration:[element.y.duration getWithDefaultValue:1] forView:view animationName:[NSString stringWithFormat:@"%@.position.y", elementName]];
	[self animationWithKeyPath:@"opacity" from:@([element.alpha.from getWithDefaultValue:1]) to:@([element.alpha.to getWithDefaultValue:1]) duration:[element.alpha.duration getWithDefaultValue:1] forView:view animationName:[NSString stringWithFormat:@"%@.alpha", elementName]];
}

@end
