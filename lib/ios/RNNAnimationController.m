#import <React/RCTRedBox.h>
#import "RNNAnimationController.h"
#import "RNNSharedElementView.h"
#import "RNNInteractivePopController.h"


@interface  RNNAnimationController()
@property (nonatomic, strong)NSArray* transitions;
@property (nonatomic)double duration;
@property (nonatomic)double springDamping;
@property (nonatomic, strong) RNNInteractivePopController* interactivePopController;
@property (nonatomic) BOOL backButton;
@end

@implementation RNNAnimationController

-(void)setupTransition:(NSDictionary*)data{
	if ([data objectForKey:@"transitions"]) {
		self.transitions= [data objectForKey:@"transitions"];
	} else {
		[[NSException exceptionWithName:NSInvalidArgumentException reason:@"No transitions" userInfo:nil] raise];
	}
	if ([data objectForKey:@"duration"]) {
		self.duration = [[data objectForKey:@"duration"] doubleValue];
	} else {
		self.duration = 0.7;
	}
	if ([data objectForKey:@"springDamping"]) {
		self.springDamping = [[data objectForKey:@"springDamping"] doubleValue];
	} else {
		self.springDamping = 0.85;
	}
	self.backButton = false;
}

-(NSArray*)findRNNSharedElementViews:(UIView*)view{
	NSMutableArray* sharedElementViews = [NSMutableArray new];
	for(UIView *aView in view.subviews){
		if([aView isMemberOfClass:[RNNSharedElementView class]]){
			[sharedElementViews addObject:aView];
		} else{
			if ([aView subviews]) {
				[sharedElementViews addObjectsFromArray:[self findRNNSharedElementViews:aView]];
			}
		}
	}
	return sharedElementViews;
}

-(RNNSharedElementView*)findViewToShare:(NSArray*)RNNSharedElementViews withId:(NSString*)elementId{
	for (RNNSharedElementView* sharedView in RNNSharedElementViews) {
		if ([sharedView.elementId isEqualToString:elementId]){
			return sharedView;
		}
	}

	[[NSException exceptionWithName:NSInvalidArgumentException reason:[NSString stringWithFormat:@"elementId %@ does not exist", elementId] userInfo:nil] raise];
	return nil;
}

-(NSArray*)prepareSharedElementTransition:(NSArray*)RNNSharedElementViews
						withContainerView:(UIView*)containerView
{
	NSMutableArray* sharedElementsData = [NSMutableArray new];
	for (NSDictionary* transition in self.transitions) {
		if ([transition[@"type"] isEqualToString:@"sharedElement"]){
			RNNSharedElementView* fromElement = [self findViewToShare:RNNSharedElementViews withId:transition[@"fromId"]];
			RNNSharedElementView* toElement = [self findViewToShare:RNNSharedElementViews withId:transition[@"toId"]];
			CGRect originFrame = [self frameFromSuperView:[fromElement subviews][0]];
			CGRect toFrame = [self frameFromSuperView:[toElement subviews][0]];
			UIView* animationView = nil;
			if ([fromElement type]) {
				if([[fromElement type] isEqualToString:@"image"]){
					animationView = [[UIImageView alloc] initWithImage:[[fromElement subviews][0] image]];
				} else {
					animationView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
				}
			} else {
			  animationView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
			}
			animationView.contentMode = UIViewContentModeScaleAspectFit;
			if (!self.backButton){
				animationView.frame = originFrame;
			} else {
				animationView.frame = toFrame;
			}
			animationView.clipsToBounds = true;
			[containerView addSubview:animationView];
			[fromElement setHidden: YES];
			[toElement setHidden: YES];
			[containerView bringSubviewToFront:animationView];
			
			NSNumber* interactiveImagePop = @(0);
			if ([transition objectForKey:@"interactiveImagePop"]) {
				interactiveImagePop = [transition objectForKey:@"interactiveImagePop"];
			}
			NSDictionary* sharedElementData = @{@"animationView" : animationView ,
												@"topFrame" : [NSValue valueWithCGRect:toFrame],
												@"fromView" : fromElement,
												@"toView" : toElement,
												@"bottomFrame" : [NSValue valueWithCGRect:originFrame],
												@"interactiveImagePop": interactiveImagePop};
			[sharedElementsData addObject:sharedElementData];
		}
	}
	return sharedElementsData;
}

-(CGRect)
frameFromSuperView:(UIView*)view{
	CGPoint sharedViewFrameOrigin = [view.superview convertPoint:view.frame.origin toView:nil];
	CGRect originRect = CGRectMake(sharedViewFrameOrigin.x, sharedViewFrameOrigin.y, view.frame.size.width, view.frame.size.height);
	return originRect;
}


- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext
{
	return self.duration;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext
{
	UIViewController* toVC   = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
	UIViewController* fromVC  = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
	UIView* containerView = [transitionContext containerView];
	toVC.view.frame = fromVC.view.frame;
	UIView* fromSnapshot = [fromVC.view snapshotViewAfterScreenUpdates:true];
	fromSnapshot.frame = fromVC.view.frame;
	[containerView addSubview:fromSnapshot];
	
	UIView* toSnapshot = [toVC.view snapshotViewAfterScreenUpdates:true];
	toSnapshot.frame = fromVC.view.frame;
	[containerView addSubview:toSnapshot];
	toSnapshot.alpha = 0;
	NSArray* fromRNNSharedElementViews = [self findRNNSharedElementViews:fromVC.view];
	NSArray* toRNNSharedElementViews = [self findRNNSharedElementViews:toVC.view];
	NSArray* RNNSharedElementViews = [toRNNSharedElementViews arrayByAddingObjectsFromArray:fromRNNSharedElementViews];
	NSArray* viewsToAnimate = [self prepareSharedElementTransition:RNNSharedElementViews withContainerView:containerView];
	
	[UIView animateWithDuration:[self transitionDuration:transitionContext ] delay:0 usingSpringWithDamping:self.springDamping initialSpringVelocity:0.8 options:UIViewAnimationOptionCurveEaseOut  animations:^{
		toSnapshot.alpha = 1;
		if (!self.backButton){
			for (NSMutableDictionary* viewData in viewsToAnimate ) {
				UIView* animtedView = viewData[@"animationView"];
				animtedView.frame = [viewData[@"topFrame"] CGRectValue];
			}
		} else {
			for (NSMutableDictionary* viewData in viewsToAnimate ) {
				UIView* animtedView = viewData[@"animationView"];
				animtedView.frame = [viewData[@"bottomFrame"] CGRectValue];
			}
		}
		
	} completion:^(BOOL finished) {
		for (NSMutableDictionary* viewData in viewsToAnimate ) {
			[viewData[@"fromView"] setHidden:NO];
			[viewData[@"toView"] setHidden:NO];
			UIView* animtedView = viewData[@"animationView"];
			[animtedView removeFromSuperview];
			if ([viewData[@"interactiveImagePop"] boolValue]) {
				self.interactivePopController = [[RNNInteractivePopController alloc] initWithTopView:viewData[@"toView"] andBottomView:viewData[@"fromView"] andOriginFrame:[viewData[@"bottomFrame"] CGRectValue] andViewController:toVC];
				UIPanGestureRecognizer* gesture = [[UIPanGestureRecognizer alloc] initWithTarget:self.interactivePopController
																						  action:@selector(handleGesture:)];
				[viewData[@"toView"] addGestureRecognizer:gesture];
			}
		}
		[fromSnapshot removeFromSuperview];
		[toSnapshot removeFromSuperview];
		
		if (![transitionContext transitionWasCancelled]) {
			[containerView addSubview: toVC.view];
			[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
			self.backButton = true;
		}
	}];
}
@end



