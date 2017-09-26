#import <React/RCTRedBox.h>
#import "RNNAnimationController.h"
#import "RNNSharedElementView.h"
#import "RNNInteractivePopController.h"

@interface  RNNAnimationController()
@property (nonatomic, strong)NSArray* animations;
@property (nonatomic)double duration;
@property (nonatomic)double springDamping;
@property (nonatomic)double springVelocity;
@property (nonatomic, strong) RNNInteractivePopController* interactivePopController;
@property (nonatomic) BOOL backButton;
@end

@implementation RNNAnimationController

-(void)setupTransition:(NSDictionary*)data{
	if ([data objectForKey:@"animations"]) {
		self.animations= [data objectForKey:@"animations"];
	} else {
		[[NSException exceptionWithName:NSInvalidArgumentException reason:@"No animations" userInfo:nil] raise];
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
	if ([data objectForKey:@"springVelocity"]) {
		self.springVelocity= [[data objectForKey:@"springVelocity"] doubleValue];
	} else {
		self.springVelocity = 0.8;
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
	return nil;
}

-(CGRect)frameFromSuperView:(UIView*)view{
	CGPoint sharedViewFrameOrigin = [view.superview convertPoint:view.frame.origin toView:nil];
	CGRect originRect = CGRectMake(sharedViewFrameOrigin.x, sharedViewFrameOrigin.y, view.frame.size.width, view.frame.size.height);
	return originRect;
}

-(CGPoint)centerFromSuperView:(UIView*)view{
	CGPoint sharedViewFrameOrigin = [view.superview convertPoint:view.frame.origin toView:nil];
	CGRect originRect = CGRectMake(sharedViewFrameOrigin.x, sharedViewFrameOrigin.y, view.frame.size.width, view.frame.size.height);
	CGFloat x = originRect.origin.x + view.frame.size.width/2;
	CGFloat y = originRect.origin.y + view.frame.size.height/2;
	CGPoint center = CGPointMake(x, y);
	return center;
}

-(NSArray*)prepareSharedElementTransition:(NSArray*)RNNSharedElementsToVC
						andfromVCElements:(NSArray*)RNNSharedElementsFromVC
						withContainerView:(UIView*)containerView
{
	NSMutableArray* sharedElementsData = [NSMutableArray new];
	for (NSDictionary* transition in self.animations) {
		NSNumber* startDelay = @(0);
		NSNumber* duration = @(1);
		NSNumber* springDamping = [NSNumber numberWithDouble:self.springDamping];
		NSNumber* springVelocity = [NSNumber numberWithDouble:self.springVelocity];
		if ([transition objectForKey:@"springDamping"]) {
			springDamping = transition[@"springDamping"];
		}
		if ([transition objectForKey:@"springVelocity"]) {
			springVelocity = transition[@"springVelocity"];
		}
		if ([transition objectForKey:@"startDelay"]) {
			startDelay = transition[@"startDelay"];
		}
		if ([transition objectForKey:@"duration"]) {
			duration = transition[@"duration"];
		}
		NSNumber* interactiveImagePop = @(0);
		if ([transition objectForKey:@"interactiveImagePop"]) {
			interactiveImagePop = [transition objectForKey:@"interactiveImagePop"];
		}
		NSString* elementVC = nil;
		RNNSharedElementView* fromElement = nil;
		if ([self findViewToShare:RNNSharedElementsToVC withId:transition[@"fromId"]]) {
			fromElement = [self findViewToShare:RNNSharedElementsToVC withId:transition[@"fromId"]];
			elementVC = @"toVC";
		} else if ([self findViewToShare:RNNSharedElementsFromVC withId:transition[@"fromId"]]){
			fromElement = [self findViewToShare:RNNSharedElementsFromVC withId:transition[@"fromId"]];
			elementVC = @"fromVC";
		} else {
			[[NSException exceptionWithName:NSInvalidArgumentException reason:[NSString stringWithFormat:@"elementId %@ does not exist", transition[@"fromId"]] userInfo:nil] raise];
		}
		CGSize originSize = [fromElement subviews][0].frame.size;
		CGPoint originCenter = [self centerFromSuperView:[fromElement subviews][0]];
		CGRect originFrame = [self frameFromSuperView:[fromElement subviews][0]];
		CGSize toSize = [fromElement subviews][0].frame.size;
		CGPoint toCenter = originCenter;
		CGRect toFrame = originFrame;
		UIView* toElement = nil;
		if ([transition objectForKey:@"toId"]) {
			if ([self findViewToShare:RNNSharedElementsToVC withId:transition[@"toId"]]) {
				toElement = [self findViewToShare:RNNSharedElementsToVC withId:transition[@"toId"]];
			} else if ([self findViewToShare:RNNSharedElementsFromVC withId:transition[@"toId"]]){
				toElement = [self findViewToShare:RNNSharedElementsFromVC withId:transition[@"toId"]];
			}
			toFrame = [self frameFromSuperView:[toElement subviews][0]];
			toSize = [toElement subviews][0].frame.size;
			toCenter = [self centerFromSuperView:[toElement subviews][0]];
		}
		CGAffineTransform toTransform = CGAffineTransformMakeScale(toSize.width/originSize.width ,toSize.height/originSize.height);
		CGAffineTransform transformBack = CGAffineTransformMakeScale(originSize.width/toSize.width ,originSize.height/toSize.height);
		
		CGPoint fromCenter = originCenter;
		if ([transition objectForKey:@"startY"]) {
			fromCenter.y = originCenter.y + [transition[@"startY"] doubleValue];
		}
		if ([transition objectForKey:@"startX"]) {
			fromCenter.x = originCenter.x + [transition[@"startX"] doubleValue];
		}
		if ([transition objectForKey:@"endY"]) {
			toCenter.y = originCenter.y + [transition[@"endY"] doubleValue];
		}
		if ([transition objectForKey:@"endX"]) {
			toCenter.x = originCenter.x + [transition[@"endX"] doubleValue];
		}
		UIView* animationView = nil;
		if ([fromElement type] && [[fromElement type] isEqualToString:@"image"]) {
			animationView = [[UIImageView alloc] initWithImage:[[fromElement subviews][0] image]];
		} else {
			if (!self.backButton) {
				if ([elementVC isEqualToString:@"fromVC"]) {
					animationView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
				} else {
					animationView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:YES];
				}
			} else {
				if ([elementVC isEqualToString:@"toVC"]) {
					animationView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
				} else {
					animationView = [[fromElement subviews][0] snapshotViewAfterScreenUpdates:NO];
				}
			}
		}
		if (!self.backButton){
			
			animationView.frame = CGRectMake(0, 0, originSize.width, originSize.height);
			animationView.center = fromCenter;
		} else {
			
			animationView.frame = CGRectMake(0, 0, toSize.width, toSize.height);
			animationView.center = toCenter;
		}
		animationView.contentMode = UIViewContentModeScaleAspectFit;
		
		NSMutableDictionary* elementData = [NSMutableDictionary new];
		if ([transition[@"type"] isEqualToString:@"sharedElement"]){
			[toElement setHidden: YES];
		}
		NSNumber* startAlpha = @(1);
		if ([transition objectForKey:@"startAlpha"]) {
			animationView.alpha = [transition[@"startAlpha"] doubleValue];
		}
		NSNumber* endAlpha = @(1);
		if ([transition objectForKey:@"endAlpha"]) {
			endAlpha = transition[@"endAlpha"];
		}
		[fromElement setHidden:YES];
		[containerView addSubview:animationView];
		[containerView bringSubviewToFront:animationView];
		NSDictionary* elementDataDict = @{@"animationView" : animationView ,
										  @"transition": @"translate",
										  @"view" : fromElement,
										  @"endAlpha": endAlpha,
										  @"startAlpha": startAlpha,
										  @"toCenter": [NSValue valueWithCGPoint:toCenter],
										  @"originCenter": [NSValue valueWithCGPoint:fromCenter],
										  @"toTransform": [NSValue valueWithCGAffineTransform:toTransform],
										  @"transformBack": [NSValue valueWithCGAffineTransform:transformBack],
										  @"startDelay" : startDelay,
										  @"springDamping": springDamping,
										  @"springVelocity": springVelocity,
										  @"duration" : duration,
										  @"interactiveImagePop": interactiveImagePop};
		[elementData addEntriesFromDictionary:elementDataDict];
		if ([transition[@"type"] isEqualToString:@"sharedElement"]){
			
			NSDictionary* sharedElementDict = @{
												@"transition": @"sharedElement",
												@"topFrame" : [NSValue valueWithCGRect:toFrame],
												@"bottomFrame" : [NSValue valueWithCGRect:originFrame],
												@"fromView" : fromElement,
												@"toView" : toElement
												};
			[elementData addEntriesFromDictionary:sharedElementDict];
		}
		[sharedElementsData addObject:elementData];
	}
	
	return sharedElementsData;
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
	[containerView addSubview:toVC.view];
	toVC.view.alpha = 0;
	NSArray* fromRNNSharedElementViews = [self findRNNSharedElementViews:fromVC.view];
	NSArray* toRNNSharedElementViews = [self findRNNSharedElementViews:toVC.view];
	NSArray* viewsToAnimate = [self prepareSharedElementTransition:toRNNSharedElementViews andfromVCElements:fromRNNSharedElementViews withContainerView:containerView];
	[UIView animateWithDuration:[self transitionDuration:transitionContext ] delay:0 usingSpringWithDamping:self.springDamping initialSpringVelocity:self.springVelocity options:UIViewAnimationOptionCurveEaseOut  animations:^{
		toVC.view.alpha = 1;
	} completion:^(BOOL finished) {
		for (NSMutableDictionary* viewData in viewsToAnimate ) {
			if ([viewData[@"transition"] isEqualToString:@"sharedElement"]) {
				[viewData[@"fromView"] setHidden:NO];
				[viewData[@"toView"] setHidden:NO];
			} else {
				[viewData[@"view"] setHidden:NO];
			}
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
		
		if (![transitionContext transitionWasCancelled]) {
			
			toVC.view.alpha = 1;
			
			[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
			self.backButton = true;
		}
	}];
	if (!self.backButton){
		for (NSMutableDictionary* viewData in viewsToAnimate ) {
			[UIView animateWithDuration:[viewData[@"duration"] doubleValue] delay:[viewData[@"startDelay"] doubleValue] usingSpringWithDamping:[viewData[@"springDamping"] doubleValue] initialSpringVelocity:[viewData[@"springVelocity"] doubleValue] options:UIViewAnimationOptionCurveEaseOut  animations:^{
				UIView* animtedView = viewData[@"animationView"];
				animtedView.alpha = [viewData[@"endAlpha"] doubleValue];
				animtedView.center = [viewData[@"toCenter"] CGPointValue];
				animtedView.transform = [viewData[@"toTransform"] CGAffineTransformValue];
				
			} completion:^(BOOL finished) {
				
			}];
			
		}
	} else {
		for (NSMutableDictionary* viewData in viewsToAnimate ) {
			[UIView animateWithDuration:[viewData[@"duration"] doubleValue] delay:[viewData[@"startDelay"] doubleValue] usingSpringWithDamping:[viewData[@"springDamping"] doubleValue] initialSpringVelocity:[viewData[@"springVelocity"] doubleValue] options:UIViewAnimationOptionCurveEaseOut  animations:^{
				UIView* animtedView = viewData[@"animationView"];
				animtedView.alpha = [viewData[@"startAlpha"] doubleValue];
				animtedView.center = [viewData[@"originCenter"] CGPointValue];
				animtedView.transform = [viewData[@"transformBack"] CGAffineTransformValue];
				
			} completion:^(BOOL finished) {
				
			}];
			
			
		}
	}
}
@end



