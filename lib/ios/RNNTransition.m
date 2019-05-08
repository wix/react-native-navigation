#import "RNNTransition.h"
#import "RNNElementFinder.h"
#import "RNNTransitionStateHolder.h"
#import "RNNViewLocation.h"
#import "RNNInteractivePopAnimator.h"

@interface RNNTransition () {
	UIViewController* _toVC;
	UIViewController* _fromVC;
	BOOL _isBackButton;
}

@property (nonatomic, strong) RNNElementView* fromElement;
@property (nonatomic, strong) RNNElementView* toElement;
@property (nonatomic, strong) RNNViewLocation* locations;

@end

@implementation RNNTransition

- (instancetype)initFromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC transitionOptions:(RNNTransitionStateHolder *)transitionOptions isBackButton:(BOOL)isBackButton {
	self = [super init];
	
	_toVC = toVC;
	_fromVC = fromVC;
	_isBackButton = isBackButton;
	
	self.options = transitionOptions;
	
	RNNElementFinder* elementFinder = [[RNNElementFinder alloc] initWithToVC:toVC andfromVC:fromVC];
	self.fromElement = [elementFinder findElementForId:self.options.fromId];
	self.toElement = [elementFinder findElementForId:transitionOptions.toId];
	self.locations = [[RNNViewLocation alloc] initWithFromElement:self.fromElement toElement:self.toElement startPoint:CGPointMake(self.options.startX, self.options.startY) endPoint:CGPointMake(self.options.endX, self.options.endY) andVC:fromVC];
	
	self.animatedView = [[RNNAnimatedView alloc] initFromElement:self.fromElement toElement:self.toElement andLocation:self.locations andIsBackButton:isBackButton startAlpha:self.options.startAlpha endAlpha:self.options.endAlpha];
	
	if (transitionOptions.isSharedElementTransition) {
		[self.toElement setHidden: YES];
	}
	
	[self.fromElement setHidden:YES];
	
	return self;
}

- (void)animate {
	[UIView animateWithDuration:self.options.duration delay:self.options.startDelay usingSpringWithDamping:self.options.springDamping initialSpringVelocity:self.options.springVelocity options:UIViewAnimationOptionCurveEaseOut  animations:^{
		[self setAnimatedViewFinalProperties];
	} completion:^(BOOL finished) {
		
	}];
}

- (void)setAnimatedViewFinalProperties {
	CGFloat alpha = _isBackButton ? self.options.startAlpha : self.options.endAlpha;
	self.animatedView.alpha = alpha;
	
	CGPoint center = _isBackButton ? self.locations.fromCenter : self.locations.toCenter;
	self.animatedView.center = center;
	
	CGAffineTransform transform = _isBackButton ? self.locations.transformBack : self.locations.transform;
	self.animatedView.transform = transform;
	
	RNNElementView* fromElement = _isBackButton ? self.toElement : self.fromElement;
	RNNElementView* toElement = _isBackButton ? self.fromElement : self.toElement;
	
	if (self.options.isSharedElementTransition) {
		if ([[fromElement subviews][0] isKindOfClass:[UIImageView class]]) {
			self.animatedView.contentMode = UIViewContentModeScaleAspectFill;
			if ([toElement resizeMode]){
				self.animatedView.contentMode = [RNNAnimatedView contentModefromString:[toElement resizeMode]];
			}
		}
	}
}


- (void)transitionCompleted {
	[self.fromElement setHidden:NO];
	if (self.options.isSharedElementTransition) {
		[self.toElement setHidden:NO];
	}
	
	[self.animatedView removeFromSuperview];
	
	if (self.options.interactivePop) {
		RNNInteractivePopAnimator* interactivePopAnimator = [[RNNInteractivePopAnimator alloc] initWithTopView:self.toElement andBottomView:self.fromElement andOriginFrame:self.locations.fromFrame andViewController:_toVC];
		UIPanGestureRecognizer* gesture = [[UIPanGestureRecognizer alloc] initWithTarget:interactivePopAnimator
																				  action:@selector(handleGesture:)];
		[self.toElement addGestureRecognizer:gesture];
	}
}

@end
