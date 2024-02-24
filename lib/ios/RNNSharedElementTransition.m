#import "RNNSharedElementTransition.h"
#import "RNNAnchorTransition.h"
#import "RNNAnimatedTextView.h"
#import "RNNAnimatedViewFactory.h"
#import "RNNBoundsTransition.h"
#import "RNNCenterTransition.h"
#import "RNNColorTransition.h"
#import "RNNCornerRadiusTransition.h"
#import "RNNPathTransition.h"
#import "RNNRectTransition.h"
#import "RNNRotationTransition.h"
#import "RNNTextStorageTransition.h"
#import "RNNTransformRectTransition.h"
#import "UIImageView+Transition.h"

@implementation RNNSharedElementTransition {
    RNNSharedElementTransitionOptions *_transitionOptions;
    UIView *_fromView;
    UIView *_toView;
    UIView *_containerView;
}

- (instancetype)initWithTransitionOptions:(RNNSharedElementTransitionOptions *)transitionOptions
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

- (RNNAnimatedReactView *)createAnimatedView:(RNNSharedElementTransitionOptions *)transitionOptions
                                 fromView:(UIView *)fromView
                                   toView:(UIView *)toView {
    return [RNNAnimatedViewFactory createFromElement:fromView
                                        toElement:toView
                                transitionOptions:transitionOptions];
}

- (NSMutableArray<id<RNNDisplayLinkAnimation>> *)createAnimations {
    NSMutableArray *animations = [super createAnimations:_transitionOptions];
    CGFloat startDelay = [_transitionOptions.startDelay withDefault:0];
    CGFloat duration = [_transitionOptions.duration withDefault:300];
    id<RNNInterpolatorProtocol> interpolator = _transitionOptions.interpolator;

    if (!CGRectEqualToRect(self.view.location.fromBounds, self.view.location.toBounds)) {
        [animations addObject:[[RNNBoundsTransition alloc] initWithView:self.view
                                                                from:self.view.location.fromBounds
                                                                  to:self.view.location.toBounds
                                                          startDelay:startDelay
                                                            duration:duration
                                                        interpolator:interpolator]];
    }

    if (!CGPointEqualToPoint(self.view.location.fromCenter, self.view.location.toCenter)) {
        [animations addObject:[[RNNCenterTransition alloc] initWithView:self.view
                                                                from:self.view.location.fromCenter
                                                                  to:self.view.location.toCenter
                                                          startDelay:startDelay
                                                            duration:duration
                                                        interpolator:interpolator]];
    }

    if (![_fromView.backgroundColor isEqual:_toView.backgroundColor]) {
        [animations addObject:[[RNNColorTransition alloc] initWithView:self.view
                                                               from:_fromView.backgroundColor
                                                                 to:_toView.backgroundColor
                                                         startDelay:startDelay
                                                           duration:duration
                                                       interpolator:interpolator]];
    }

    if ([self.view isKindOfClass:RNNAnimatedTextView.class]) {
        [animations addObject:[[RNNTextStorageTransition alloc]
                                  initWithView:self.view
                                          from:((RNNAnimatedTextView *)self.view).fromTextStorage
                                            to:((RNNAnimatedTextView *)self.view).toTextStorage
                                    startDelay:startDelay
                                      duration:duration
                                  interpolator:interpolator]];
    }

    if (!CGRectEqualToRect(self.view.location.fromPath, self.view.location.toPath)) {
        [animations
            addObject:[[RNNPathTransition alloc] initWithView:self.view
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
            addObject:[[RNNTransformRectTransition alloc] initWithView:self.view
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
        [animations addObject:[[RNNCornerRadiusTransition alloc]
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
