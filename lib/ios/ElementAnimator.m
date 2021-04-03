#import "ElementAnimator.h"
#import "ElementAlphaTransition.h"
#import "ElementHorizontalTransition.h"
#import "ElementVerticalTransition.h"
#import "HorizontalTranslationTransition.h"
#import "RNNElementFinder.h"
#import "Transition.h"
#import "VerticalRotationTransition.h"
#import "VerticalTranslationTransition.h"

@implementation ElementAnimator {
    UIView *_containerView;
}

- (instancetype)initWithTransitionOptions:(TransitionOptions *)transitionOptions
                                     view:(UIView *)view
                            containerView:(UIView *)containerView {
    self = [super init];
    _containerView = containerView;
    self.view = view;
    self.animations = [self createAnimations:transitionOptions];
    return self;
}

- (NSMutableArray<id<DisplayLinkAnimation>> *)createAnimations:
    (TransitionOptions *)transitionOptions {
    NSMutableArray *animations = [NSMutableArray new];
    if (transitionOptions.alpha.hasAnimation) {
        [animations
            addObject:[[ElementAlphaTransition alloc] initWithView:self.view
                                                 transitionDetails:transitionOptions.alpha]];
    }

    if (transitionOptions.x.hasAnimation) {
        [animations
            addObject:[[ElementHorizontalTransition alloc] initWithView:self.view
                                                      transitionDetails:transitionOptions.x]];
    }

    if (transitionOptions.y.hasAnimation) {
        [animations addObject:[[ElementVerticalTransition alloc] initWithView:self.view
                                                            transitionDetails:transitionOptions.y]];
    }

    if (transitionOptions.translationX.hasAnimation) {
        [animations addObject:[[HorizontalTranslationTransition alloc]
                                       initWithView:self.view
                                  transitionDetails:transitionOptions.translationX]];
    }

    if (transitionOptions.translationY.hasAnimation) {
        [animations addObject:[[VerticalTranslationTransition alloc]
                                       initWithView:self.view
                                  transitionDetails:transitionOptions.translationY]];
    }

    if (transitionOptions.rotationY.hasAnimation) {
        [animations addObject:[[VerticalRotationTransition alloc]
                                       initWithView:self.view
                                  transitionDetails:transitionOptions.rotationY]];
    }

    return animations;
}

@end
