#import "SharedElementTransition.h"
#import "AnchorTransition.h"
#import "AnimatedTextView.h"
#import "AnimatedViewFactory.h"
#import "BoundsTransition.h"
#import "CenterTransition.h"
#import "ColorTransition.h"
#import "CornerRadiusTransition.h"
#import "PathTransition.h"
#import "RectTransition.h"
#import "RotationTransition.h"
#import "TextStorageTransition.h"
#import "TransformRectTransition.h"
#import "UIImageView+Transition.h"

@implementation SharedElementTransition {
    SharedElementTransitionOptions *_transitionOptions;
    UIView *_fromView;
    UIView *_toView;
    UIView *_containerView;
}

- (instancetype)initWithTransitionOptions:(SharedElementTransitionOptions *)transitionOptions
                                 fromView:(UIView *)fromView
                                   toView:(UIView *)toView
                            containerView:(UIView *)containerView {
    self = [super init];
    _transitionOptions = transitionOptions;
    _fromView = fromView;
    _toView = toView;
    _containerView = containerView;
    self.view = [self createAnimatedView:transitionOptions fromView:fromView toView:toView];
    _parentView = self.view.superview;
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

    if (!CGRectEqualToRect(self.view.location.fromBounds, self.view.location.toBounds)) {
        [animations addObject:[[BoundsTransition alloc] initWithView:self.view
                                                                from:self.view.location.fromBounds
                                                                  to:self.view.location.toBounds
                                                          startDelay:startDelay
                                                            duration:duration
                                                        interpolator:interpolator]];
    }

    if (!CGPointEqualToPoint(self.view.location.fromCenter, self.view.location.toCenter)) {
        [animations addObject:[[CenterTransition alloc] initWithView:self.view
                                                                from:self.view.location.fromCenter
                                                                  to:self.view.location.toCenter
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

    if (!CGRectEqualToRect(self.view.location.fromPath, self.view.location.toPath)) {
        [animations
            addObject:[[PathTransition alloc] initWithView:self.view
                                                  fromPath:self.view.location.fromPath
                                                    toPath:self.view.location.toPath
                                          fromCornerRadius:self.view.location.fromCornerRadius
                                            toCornerRadius:self.view.location.toCornerRadius
                                                startDelay:startDelay
                                                  duration:duration
                                              interpolator:interpolator]];
    }

    if (!CATransform3DEqualToTransform(self.view.location.fromTransform,
                                       self.view.location.toTransform)) {
        [animations
            addObject:[[TransformRectTransition alloc] initWithView:self.view
                                                               from:self.view.location.fromTransform
                                                                 to:self.view.location.toTransform
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

    [animations addObjectsFromArray:self.view.extraAnimations];

    return animations;
}

@end
