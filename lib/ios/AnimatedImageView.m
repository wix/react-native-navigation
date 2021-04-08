#import "AnimatedImageView.h"
#import "BoundsTransition.h"
#import "CenterTransition.h"
#import "UIImageView+Transition.h"

@implementation AnimatedImageView {
    SharedElementTransitionOptions *_transitionOptions;
    CGRect _originalBounds;
    CGPoint _originalCenter;
    UIViewContentMode _originalContentMode;
}

- (instancetype)initElement:(UIView *)element
                  toElement:(UIView *)toElement
          transitionOptions:(SharedElementTransitionOptions *)transitionOptions {
    self = [super initElement:element toElement:toElement transitionOptions:transitionOptions];

    _transitionOptions = transitionOptions;

    _fromImageView = [self findImageView:element];
    _toImageView = [self findImageView:toElement];
    _originalBounds = _fromImageView.bounds;
    _originalContentMode = _fromImageView.contentMode;
    _originalCenter = _fromImageView.center;
    _fromImageView.autoresizingMask =
        UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;

    return self;
}

- (UIImageView *)findImageView:(UIView *)view {
    if ([view isKindOfClass:UIImageView.class]) {
        return (UIImageView *)view;
    }

    for (UIView *subview in view.subviews) {
        return [self findImageView:subview];
    }

    return nil;
}

- (NSArray<id<DisplayLinkAnimation>> *)extraAnimations {
    NSMutableArray *animations = NSMutableArray.new;
    CGFloat startDelay = [_transitionOptions.startDelay withDefault:0];
    CGFloat duration = [_transitionOptions.duration withDefault:300];
    id<Interpolator> interpolator = _transitionOptions.interpolator;

    [animations addObject:[[BoundsTransition alloc] initWithView:_fromImageView
                                                            from:_fromImageView.resolveBounds
                                                              to:_toImageView.resolveBounds
                                                      startDelay:startDelay
                                                        duration:duration
                                                    interpolator:interpolator]];

    [animations addObject:[[CenterTransition alloc] initWithView:_fromImageView
                                                            from:_fromImageView.center
                                                              to:_toImageView.center
                                                      startDelay:startDelay
                                                        duration:duration
                                                    interpolator:interpolator]];
    _fromImageView.contentMode = UIViewContentModeScaleToFill;

    return animations;
}

- (void)reset {
    [super reset];
    _fromImageView.bounds = _originalBounds;
    _fromImageView.contentMode = _originalContentMode;
    _fromImageView.center = _originalCenter;
}

@end
