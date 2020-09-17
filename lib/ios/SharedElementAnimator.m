#import "SharedElementAnimator.h"
#import "AnimatedViewFactory.h"
#import "RectTransition.h"
#import "TransformRectTransition.h"
#import "RotationTransition.h"
#import "ColorTransition.h"
#import "AnimatedTextView.h"
#import "TextStorageTransition.h"
#import "AnchorTransition.h"
#import "CornerRadiusTransition.h"
#import "LayerBoundsTransition.h"

@implementation SharedElementAnimator {
    SharedElementTransitionOptions* _transitionOptions;
    UIViewController* _toVC;
    UIViewController* _fromVC;
    UIView* _fromView;
    UIView* _toView;
    UIView* _containerView;
}

- (instancetype)initWithTransitionOptions:(SharedElementTransitionOptions *)transitionOptions fromView:(UIView *)fromView toView:(UIView *)toView fromVC:(UIViewController *)fromVC toVC:(UIViewController *)toVC containerView:(UIView *)containerView {
    self = [super init];
    _transitionOptions = transitionOptions;
    _fromVC = fromVC;
    _toVC = toVC;
    _fromView = fromView;
    _toView = toView;
    _containerView = containerView;
    self.view = [self createAnimatedView:transitionOptions fromView:fromView toView:toView];
    self.animations = [self createAnimations];
    return self;
}

- (AnimatedReactView *)createAnimatedView:(SharedElementTransitionOptions *)transitionOptions fromView:(UIView *)fromView toView:(UIView *)toView {
    return [AnimatedViewFactory createFromElement:fromView toElement:toView transitionOptions:transitionOptions];
}

- (NSMutableArray<id<DisplayLinkAnimation>> *)createAnimations {
    NSMutableArray* animations = [super createAnimations:_transitionOptions];
    CGFloat startDelay = [_transitionOptions.startDelay getWithDefaultValue:0];
    CGFloat duration = [_transitionOptions.duration getWithDefaultValue:300];
    Text* interpolation = [_transitionOptions.interpolation getWithDefaultValue:@"accelerateDecelerate"];
    
    if (!CGRectEqualToRect(self.view.location.fromFrame, self.view.location.toFrame)) {
        [animations addObject:[[RectTransition alloc] initWithView:self.view from:self.view.location.fromFrame to:self.view.location.toFrame startDelay:startDelay duration:duration interpolation:interpolation]];
    }
    
    if (!CGRectEqualToRect(self.view.location.fromBounds, self.view.location.toBounds)) {
        [animations addObject:[[LayerBoundsTransition alloc] initWithView:self.view from:self.view.location.fromBounds to:self.view.location.toBounds startDelay:startDelay duration:duration interpolation:interpolation]];
    }
    
    if (!CATransform3DEqualToTransform(self.view.location.fromTransform, self.view.location.toTransform)) {
        [animations addObject:[[TransformRectTransition alloc] initWithView:self.view viewLocation:self.view.location startDelay:startDelay duration:duration interpolation:interpolation]];
    }
    
    if (![_fromView.backgroundColor isEqual:_toView.backgroundColor]) {
        [animations addObject:[[ColorTransition alloc] initWithView:self.view from:_fromView.backgroundColor to:_toView.backgroundColor startDelay:startDelay duration:duration interpolation:interpolation]];
    }
    
    if ([self.view isKindOfClass:AnimatedTextView.class]) {
        [animations addObject:[[TextStorageTransition alloc] initWithView:self.view from:((AnimatedTextView *)self.view).fromTextStorage to:((AnimatedTextView *)self.view).toTextStorage startDelay:startDelay duration:duration interpolation:interpolation]];
    }
	
	if (_fromView.layer.cornerRadius != _toView.layer.cornerRadius) {
		// TODO: Use MaskedCorners to only round specific corners, e.g.: borderTopLeftRadius
		//   self.view.layer.maskedCorners = kCALayerMinXMinYCorner | kCALayerMaxXMinYCorner | kCALayerMinXMaxYCorner | kCALayerMaxXMaxYCorner;
		// TODO: On pop the cornerRadius animation doesn't work, even though the CornerRadiusTransition::animateWithProgress function is called.
		self.view.layer.masksToBounds = YES;
		[animations addObject:[[CornerRadiusTransition alloc] initWithView:self.view fromFloat:_fromView.layer.cornerRadius toFloat:_toView.layer.cornerRadius startDelay:startDelay duration:duration interpolation:interpolation]];
	}
    
    return animations;
}

- (void)end {
    [super end];
    [self.view reset];
}


@end
