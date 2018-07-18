#import "RNNAnimator.h"
#import "RNNTransition.h"

@interface  RNNAnimator()
@property (nonatomic, strong) RNNAnimationOptions* transitionOptions;
@property (nonatomic) BOOL backButton;
@property (nonatomic, weak) UIViewController* fromVC;
@property (nonatomic, weak) UIViewController* toVC;
@end

@implementation RNNAnimator

-(instancetype)initWithTransitionOptions:(RNNAnimationOptions *)transitionOptions {
	self = [super init];
	if (transitionOptions.animations) {
		[self setupTransition:transitionOptions];
	} else {
		return nil;
	}
	
	return self;
}

-(void)setupTransition:(RNNAnimationOptions *)transitionOptions {
	self.transitionOptions = transitionOptions;
	if (!transitionOptions.animations) {
		[[NSException exceptionWithName:NSInvalidArgumentException reason:@"No animations" userInfo:nil] raise];
	}
	
	self.backButton = false;
}

-(NSArray*)prepareSharedElementTransitionWithComponentView:(UIView*)componentView {
	NSMutableArray* transitions = [NSMutableArray new];
	for (NSDictionary* transition in self.transitionOptions.animations) {
		RNNTransitionStateHolder* transitionStateHolder = [[RNNTransitionStateHolder alloc] initWithDict:transition];
		RNNTransition* transition = [[RNNTransition alloc] initFromVC:self.fromVC toVC:self.toVC transitionOptions:transitionStateHolder isBackButton:self.backButton];

		[componentView addSubview:transition.animatedView];
		[componentView bringSubviewToFront:transition.animatedView];
		
		[transitions addObject:transition];
	}
	
	return transitions;
}

-(void)animateTransitions:(NSArray*)transitions {
	for (RNNTransition* transition in transitions ) {
		[transition animate];
	}
}

-(void)animateCompletion:(NSArray*)transitions fromVCSnapshot:(UIView*)fromSnapshot andTransitioningContext:(id<UIViewControllerContextTransitioning>)transitionContext {
	[UIView animateWithDuration:[self transitionDuration:transitionContext] delay:0 usingSpringWithDamping:[self.transitionOptions.springDamping doubleValue] initialSpringVelocity:[self.transitionOptions.springVelocity doubleValue] options:UIViewAnimationOptionCurveEaseOut  animations:^{
				self.toVC.view.alpha = 1;
			} completion:^(BOOL finished) {
				for (RNNTransition* transition in transitions ) {
					[transition transitionCompleted];
				}
				
				[fromSnapshot removeFromSuperview];
				if (![transitionContext transitionWasCancelled]) {
					self.toVC.view.alpha = 1;
					[transitionContext completeTransition:![transitionContext transitionWasCancelled]];
					self.backButton = true;
				}
			}];
}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext {
	return [self.transitionOptions.duration doubleValue];
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext {
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
	
	NSArray* transitions = [self prepareSharedElementTransitionWithComponentView:componentView];
	[self animateCompletion:transitions fromVCSnapshot:fromSnapshot andTransitioningContext:transitionContext];
	[self animateTransitions:transitions];
}

@end
