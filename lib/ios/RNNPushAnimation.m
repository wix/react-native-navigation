#import "RNNPushAnimation.h"

@implementation RNNPushAnimation

- (instancetype)initWithScreenTransition:(RNNScreenTransition *)screenTransition {
	self = [super init];
	self.screenTransition = screenTransition;
	return self;
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
	return self.screenTransition.maxDuration / 1000;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
	UIViewController* fromViewController = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
	UIViewController* toViewController = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
	
	[[transitionContext containerView] addSubview:fromViewController.view];
	[[transitionContext containerView] addSubview:toViewController.view];
	
	[CATransaction begin];
	[CATransaction setCompletionBlock:^{
		[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
	}];

	[self animateElement:self.screenTransition.topBar view:toViewController.navigationController.navigationBar elementName:@"topBar"];
	[self animateElement:self.screenTransition.content view:toViewController.view elementName:@"content"];
	[self animateElement:self.screenTransition.bottomTabs view:toViewController.tabBarController.tabBar elementName:@"bottomTabs"];
	
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
