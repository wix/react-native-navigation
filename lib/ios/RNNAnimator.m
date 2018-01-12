#import <React/RCTRedBox.h>
#import "RNNAnimator.h"
#import "RNNElementView.h"
#import "RNNInteractivePopAnimator.h"
#import "VICMAImageView.h"
#import "RNNTransitionStateHolder.h"
#import "RNNElementFinder.h"
#import "RNNViewLocation.h"
#import "RNNAnimatedView.h"

@interface  RNNAnimator()
@property (nonatomic, strong)NSArray* animations;
@property (nonatomic)double duration;
@property (nonatomic)double springDamping;
@property (nonatomic)double springVelocity;
@property (nonatomic, strong) RNNInteractivePopAnimator* interactivePopAnimator;
@property (nonatomic) BOOL backButton;
@property (nonatomic, strong) UIViewController* fromVC;
@property (nonatomic, strong) UIViewController* toVC;
@end

@implementation RNNAnimator

- (instancetype)initWithAnimationsDictionary:(NSDictionary *)animationsDic {
	self = [super init];
	if (animationsDic) {
		[self setupTransition:animationsDic];
	} else {
		return nil;
	}
	
	return self;
}

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

-(NSArray*)prepareSharedElementTransition:(NSArray*)RNNSharedElementsToVC
						andfromVCElements:(NSArray*)RNNSharedElementsFromVC
						withComponentView:(UIView*)componentView
{
	NSMutableArray* transitions = [NSMutableArray new];
	for (NSDictionary* transition in self.animations) {
		RNNTransitionStateHolder* transitionStateHolder = [[RNNTransitionStateHolder alloc] initWithTransition:transition];
		RNNElementFinder* elementFinder = [[RNNElementFinder alloc] initWithToVC:self.toVC andfromVC:self.fromVC];
		[elementFinder findElementsInTransition:transitionStateHolder];
		RNNViewLocation* animatedViewLocations = [[RNNViewLocation alloc] initWithTransition:transitionStateHolder andVC:self.fromVC];
		RNNAnimatedView* animatedView = [[RNNAnimatedView alloc] initWithTransition:transitionStateHolder andLocation:animatedViewLocations andIsBackButton:self.backButton];
		transitionStateHolder.locations = animatedViewLocations;
		[componentView addSubview:animatedView];
		[componentView bringSubviewToFront:animatedView];
		transitionStateHolder.animatedView = animatedView;
		[transitions addObject:transitionStateHolder];
		if (transitionStateHolder.isSharedElementTransition){
			[transitionStateHolder.toElement setHidden: YES];
		}
		[transitionStateHolder.fromElement setHidden:YES];
	}
	return transitions;
}

-(void)animateTransitions:(NSArray*)transitions {
	for (RNNTransitionStateHolder* transition in transitions ) {
		[UIView animateWithDuration:transition.duration delay:transition.startDelay usingSpringWithDamping:transition.springDamping initialSpringVelocity:transition.springVelocity options:UIViewAnimationOptionCurveEaseOut  animations:^{
			RNNAnimatedView* animatedView = transition.animatedView;
			if (!self.backButton) {
				[self setAnimatedViewFinalProperties:animatedView toElement:transition.toElement fromElement:transition.fromElement isSharedElementTransition:transition.isSharedElementTransition withTransform:transition.locations.transform withCenter:transition.locations.toCenter andAlpha:transition.endAlpha];
			} else {
				[self setAnimatedViewFinalProperties:animatedView toElement:transition.fromElement fromElement:transition.fromElement isSharedElementTransition:transition.isSharedElementTransition withTransform:transition.locations.transformBack withCenter:transition.locations.fromCenter andAlpha:transition.startAlpha];
			}
		} completion:^(BOOL finished) {

		}];
	}
}

-(void)setAnimatedViewFinalProperties:(RNNAnimatedView*)animatedView toElement:(RNNElementView*)toElement fromElement:(RNNElementView*)fromElement isSharedElementTransition:(BOOL)isShared withTransform:(CGAffineTransform)transform withCenter:(CGPoint)center andAlpha:(double)alpha {
	animatedView.alpha = alpha;
	animatedView.center = center;
	animatedView.transform = transform;
	if (isShared) {
		if ([[fromElement subviews][0] isKindOfClass:[UIImageView class]]) {
			animatedView.contentMode = UIViewContentModeScaleAspectFill;
			if ([toElement resizeMode]){
				animatedView.contentMode = [RNNAnimatedView contentModefromString:[toElement resizeMode]];
			}
		}
	}
}


-(void)animateComplition:(NSArray*)transitions fromVCSnapshot:(UIView*)fromSnapshot andTransitioningContext:(id<UIViewControllerContextTransitioning>)transitionContext {
	[UIView animateWithDuration:[self transitionDuration:transitionContext ] delay:0 usingSpringWithDamping:self.springDamping initialSpringVelocity:self.springVelocity options:UIViewAnimationOptionCurveEaseOut  animations:^{
				self.toVC.view.alpha = 1;
			} completion:^(BOOL finished) {
				for (RNNTransitionStateHolder* transition in transitions ) {
					[transition.fromElement setHidden:NO];
					if (transition.isSharedElementTransition) {
						[transition.toElement setHidden:NO];
					}
					RNNAnimatedView* animtedView = transition.animatedView;
					[animtedView removeFromSuperview];
					
					if (transition.interactivePop) {
						self.interactivePopAnimator = [[RNNInteractivePopAnimator alloc] initWithTopView:transition.toElement andBottomView:transition.fromElement andOriginFrame:transition.locations.fromFrame andViewController:self.toVC];
						UIPanGestureRecognizer* gesture = [[UIPanGestureRecognizer alloc] initWithTarget:self.interactivePopAnimator
																								  action:@selector(handleGesture:)];
						[transition.toElement addGestureRecognizer:gesture];
					}
				}
				[fromSnapshot removeFromSuperview];
				if (![transitionContext transitionWasCancelled]) {
					self.toVC.view.alpha = 1;
					[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
					self.backButton = true;
				}
			}];
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext
{
	return self.duration;
}
- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext
{
	UIViewController* toVC   = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
	UIViewController* fromVC  = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
	UIView* componentView = [transitionContext containerView];
	self.fromVC = fromVC;
	self.toVC = toVC;
	toVC.view.frame = fromVC.view.frame;
	UIView* fromSnapshot = [fromVC.view snapshotViewAfterScreenUpdates:true];
	fromSnapshot.frame = fromVC.view.frame;
	[componentView addSubview:fromSnapshot];
	[componentView addSubview:toVC.view];
	toVC.view.alpha = 0;
	NSArray* onlyForTesting = @[];
	NSArray* onlyForTesting2 = @[];
	NSArray* transitions = [self prepareSharedElementTransition:onlyForTesting andfromVCElements:onlyForTesting2 withComponentView:componentView];
	[self animateComplition:transitions fromVCSnapshot:fromSnapshot andTransitioningContext:transitionContext];
	[self animateTransitions:transitions];
}
@end



