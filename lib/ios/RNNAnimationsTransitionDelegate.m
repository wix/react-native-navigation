#import "RNNAnimationsTransitionDelegate.h"

@implementation RNNAnimationsTransitionDelegate

- (instancetype)initWithScreenTransition:(TransitionOptions *)transitionOptions isDismiss:(BOOL)isDismiss {
	self = [super init];
	self.transitionOptions = transitionOptions;
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
	return self.transitionOptions.maxDuration;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
	UIView* toView = [transitionContext viewForKey:UITransitionContextToViewKey];
	UIView* fromView = [transitionContext viewForKey:UITransitionContextFromViewKey];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
	}];
	
	if (_isDismiss) {
		[[transitionContext containerView] addSubview:toView];
		[[transitionContext containerView] addSubview:fromView];
		[self animateElement:self.transitionOptions view:fromView elementName:@"content"];
	} else {
		[[transitionContext containerView] addSubview:toView];
		[self animateElement:self.transitionOptions view:toView elementName:@"content"];
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

- (void)animateElement:(TransitionOptions *)transition view:(UIView *)view elementName:(NSString *)elementName {
	[self animationWithKeyPath:@"position.x" from:@(view.layer.position.x + [transition.x.from getWithDefaultValue:0]) to:@(view.layer.position.x + [transition.x.to getWithDefaultValue:0]) duration:[transition.x.duration getWithDefaultValue:1] forView:view animationName:@"transition.position.x"];
	[self animationWithKeyPath:@"position.y" from:@(view.layer.position.y + [transition.y.from getWithDefaultValue:0]) to:@(view.layer.position.y + [transition.y.to getWithDefaultValue:0]) duration:[transition.y.duration getWithDefaultValue:1] forView:view animationName:[NSString stringWithFormat:@"%@.position.y", elementName]];
	[self animationWithKeyPath:@"opacity" from:@([transition.alpha.from getWithDefaultValue:1]) to:@([transition.alpha.to getWithDefaultValue:1]) duration:[transition.alpha.duration getWithDefaultValue:1] forView:view animationName:[NSString stringWithFormat:@"%@.alpha", elementName]];
}

@end
