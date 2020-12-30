#import "SharedElementAnimator.h"
#import "AnchorTransition.h"
#import "AnimatedTextView.h"
#import "AnimatedViewFactory.h"
#import "ColorTransition.h"
#import "CornerRadiusTransition.h"
#import "RectTransition.h"
#import "RotationTransition.h"
#import "TextStorageTransition.h"

@implementation SharedElementAnimator {
    SharedElementTransitionOptions *_transitionOptions;
    UIViewController *_toVC;
    UIViewController *_fromVC;
    UIView *_fromView;
    UIView *_toView;
    UIView *_containerView;
}

- (instancetype)initWithTransitionOptions:(SharedElementTransitionOptions *)transitionOptions
                                 fromView:(UIView *)fromView
                                   toView:(UIView *)toView
                                   fromVC:(UIViewController *)fromVC
                                     toVC:(UIViewController *)toVC
                            containerView:(UIView *)containerView {
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

- (AnimatedReactView *)createAnimatedView:(SharedElementTransitionOptions *)transitionOptions
                                 fromView:(UIView *)fromView
                                   toView:(UIView *)toView {
    return [AnimatedViewFactory createFromElement:fromView
                                        toElement:toView
                                transitionOptions:transitionOptions];
}

- (NSMutableArray<id<DisplayLinkAnimation>> *)createAnimations {
    NSMutableArray *animations = [super createAnimations:_transitionOptions];
    CGFloat startDelay = [_transitionOptions.startDelay withDefault:0];
    CGFloat duration = [_transitionOptions.duration withDefault:300];
    id<Interpolator> interpolator = _transitionOptions.interpolator;

    if (!CGRectEqualToRect(self.view.location.fromFrame, self.view.location.toFrame)) {
        [animations addObject:[[RectTransition alloc] initWithView:self.view
                                                              from:self.view.location.fromFrame
                                                                to:self.view.location.toFrame
                                                        startDelay:startDelay
                                                          duration:duration
                                                      interpolator:interpolator]];
    }

    if (![_fromView.backgroundColor isEqual:_toView.backgroundColor]) {
        [animations addObject:[[ColorTransition alloc] initWithView:self.view
                                                               from:_fromView.backgroundColor
                                                                 to:_toView.backgroundColor
                                                         startDelay:startDelay
                                                           duration:duration
                                                       interpolator:interpolator]];
    }

    if ([self.view isKindOfClass:AnimatedTextView.class]) {
        [animations addObject:[[TextStorageTransition alloc]
                                  initWithView:self.view
                                          from:((AnimatedTextView *)self.view).fromTextStorage
                                            to:((AnimatedTextView *)self.view).toTextStorage
                                    startDelay:startDelay
                                      duration:duration
                                  interpolator:interpolator]];
    }

    if (self.view.location.fromCornerRadius != self.view.location.toCornerRadius) {
        // TODO: Use MaskedCorners to only round specific corners, e.g.:
        // borderTopLeftRadius
        //   self.view.layer.maskedCorners = kCALayerMinXMinYCorner |
        //   kCALayerMaxXMinYCorner | kCALayerMinXMaxYCorner |
        //   kCALayerMaxXMaxYCorner;
        self.view.layer.masksToBounds = YES;
        [animations addObject:[[CornerRadiusTransition alloc]
                                  initWithView:self.view
                                     fromFloat:self.view.location.fromCornerRadius
                                       toFloat:self.view.location.toCornerRadius
                                    startDelay:startDelay
                                      duration:duration
                                  interpolator:interpolator]];
    }

    return animations;
}

- (void)end {
    [super end];
    [self.view reset];
}

@end
