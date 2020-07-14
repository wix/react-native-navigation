#import "RNNInteractivePopAnimator.h"
#import "StackTransitionDelegate.h"
#import "RNNElementView.h"
#import "RNNComponentViewController.h"
#import "VICMAImageView.h"

@interface  RNNInteractivePopAnimator()
@property (nonatomic) CGRect topFrame;
@property (nonatomic) CGFloat percent;
@property (nonatomic) UINavigationController* nc;
@property (nonatomic) UIView* imageSnapshot;
@property (nonatomic) double totalTranslate;
@property (nonatomic) id transitionContext;


@end

@implementation RNNInteractivePopAnimator

-(instancetype)initWithTopView:(RNNElementView*)topView andBottomView:(RNNElementView*)bottomView andOriginFrame:(CGRect)originFrame andViewController:(UIViewController*)vc{
	RNNInteractivePopAnimator* interactiveController = [[RNNInteractivePopAnimator alloc] init];
	[interactiveController setTopView:topView];
	[interactiveController setBottomView:bottomView];
	[interactiveController setOriginFrame:originFrame];
	[interactiveController setVc:vc];
	[interactiveController setNc:vc.navigationController];
	return interactiveController;
}

-(void)startInteractiveTransition:(id<UIViewControllerContextTransitioning>)transitionContext{
	[super startInteractiveTransition:transitionContext];
}

-(BOOL)shouldBeginInteractivePop:(CGPoint)velocity {
	if (velocity.y > 0) {
		return YES;
	}
	return NO;
}

-(BOOL)shouldCancelInteractivePop:(UIPanGestureRecognizer*)recognizer {
	return ([recognizer velocityInView:self.imageSnapshot].y < 0 || self.totalTranslate < 0);
}
-(void)handleGesture:(UIPanGestureRecognizer*)recognizer {
	CGPoint translation = [recognizer translationInView:self.topView];
	if (recognizer.state == UIGestureRecognizerStateBegan) {
		CGPoint velocity = [recognizer velocityInView:recognizer.view];
		self.nc.delegate = self;
		if ([self shouldBeginInteractivePop:velocity]) {
			[self.nc popViewControllerAnimated:YES];
		}
	} else
	if (recognizer.state == UIGestureRecognizerStateChanged) {
		self.totalTranslate = self.totalTranslate + translation.y;
		[self animateAlongsideTransition:^void(id context){
			self.imageSnapshot.center = CGPointMake(self.imageSnapshot.center.x + translation.x,
													self.imageSnapshot.center.y + translation.y);
		}
							  completion:nil];
		[recognizer setTranslation:CGPointMake(0, 0) inView:self.imageSnapshot];
		if (self.totalTranslate >= 0 && self.totalTranslate <= 400.0) {
			[self updateInteractiveTransition:self.totalTranslate/400];
		}
	} else if (recognizer.state == UIGestureRecognizerStateEnded) {
		if([self shouldCancelInteractivePop:recognizer]) {
			[self cancelInteractiveTransition];
			[UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.8 initialSpringVelocity:0.8 options:UIViewAnimationOptionCurveEaseOut  animations:^{
				self.imageSnapshot.frame = self.topFrame;
			} completion:^(BOOL finished) {
				self.nc.delegate = (RNNComponentViewController*)self.vc;
			}];
		} else {
			[UIView animateWithDuration:0.3 delay:0 usingSpringWithDamping:0.8 initialSpringVelocity:0.8 options:UIViewAnimationOptionCurveEaseOut  animations:^{
				self.imageSnapshot.frame = self.originFrame;
				self.imageSnapshot.contentMode = UIViewContentModeScaleAspectFill;
			} completion:^(BOOL finished) {
				self.nc.delegate = nil;
			}];
			[self finishInteractiveTransition];
		}
	}
}
- (BOOL)animateAlongsideTransition:(void (^)(id<UIViewControllerTransitionCoordinatorContext> context))animation
						completion:(void (^)(id<UIViewControllerTransitionCoordinatorContext> context))completion;{
	animation(nil);
	return YES;
}
- (void)animationEnded:(BOOL)transitionCompleted {

}

- (NSTimeInterval)transitionDuration:(id <UIViewControllerContextTransitioning>)transitionContext
{
	return 0.7;
}

- (void)animateTransition:(id<UIViewControllerContextTransitioning>)transitionContext
{
//	self.totalTranslate = 0;
//	self.transitionContext = transitionContext;
//	UIViewController* toVC   = [transitionContext viewControllerForKey:UITransitionContextToViewControllerKey];
//	UIViewController* fromVC  = [transitionContext viewControllerForKey:UITransitionContextFromViewControllerKey];
//	UIView* componentView = [transitionContext containerView];
//
//	toVC.view.frame = fromVC.view.frame;
//	UIView* topViewContent = self.topView.view;
//	UIImage* image = [[self.topView subviews][0] image];
//	UIView* imageSnapshot = [[VICMAImageView alloc] initWithImage:image];
//	CGPoint fromSharedViewFrameOrigin = [topViewContent.superview convertPoint:topViewContent.frame.origin toView:fromVC.view];
//	CGRect fromOriginRect = CGRectMake(fromSharedViewFrameOrigin.x, fromSharedViewFrameOrigin.y, topViewContent.frame.size.width, topViewContent.frame.size.height);
//	self.topFrame = fromOriginRect;
//	imageSnapshot.contentMode = UIViewContentModeScaleAspectFill;
//	imageSnapshot.frame = fromOriginRect;
//	self.imageSnapshot = imageSnapshot;
//	[self.bottomView setHidden:YES];
//	UIView* toSnapshot = [toVC.view snapshotViewAfterScreenUpdates:true];
//	toSnapshot.frame = fromVC.view.frame;
//	[componentView insertSubview:(UIView *)toSnapshot atIndex:1];
//	[componentView addSubview:self.imageSnapshot];
//	toSnapshot.alpha = 0.0;
//	[self.topView setHidden:YES];
//	[UIView animateKeyframesWithDuration:(NSTimeInterval)[self transitionDuration:transitionContext]
//								   delay:0
//								 options: UIViewKeyframeAnimationOptionAllowUserInteraction
//							  animations:^{
//								  [UIView addKeyframeWithRelativeStartTime:0 relativeDuration:1 animations:^{
//									  fromVC.view.alpha = 0;
//									  toSnapshot.alpha = 1;
//								  }];
//							  }
//							  completion:^(BOOL finished) {
//								  [self.bottomView setHidden:NO];
//								  [toSnapshot removeFromSuperview];
//								  [self.imageSnapshot removeFromSuperview];
//								  self.totalTranslate = 0;
//								  if (![transitionContext transitionWasCancelled]) {
//									  [componentView addSubview: toVC.view];
//									  [transitionContext completeTransition:![transitionContext transitionWasCancelled]];
//
//								  }
//								  if ([transitionContext transitionWasCancelled]) {
//									  [self.topView setHidden:NO];
//									  [componentView addSubview: fromVC.view];
//									  [transitionContext completeTransition:NO];
//								  }
//							  }];
}

- (id<UIViewControllerAnimatedTransitioning>)navigationController:(UINavigationController *)navigationController
								  animationControllerForOperation:(UINavigationControllerOperation)operation
											   fromViewController:(UIViewController*)fromVC
												 toViewController:(UIViewController*)toVC {

		if (operation == UINavigationControllerOperationPop) {
			return self;
		} else {
			return nil;
		}

	return nil;

}
- (id<UIViewControllerInteractiveTransitioning>)navigationController:(UINavigationController *)navigationController
						 interactionControllerForAnimationController:(id<UIViewControllerAnimatedTransitioning>)animationController {
	return self;
}

@end
