#import "RNNElementAnimator.h"
#import "RNNElementAlphaTransition.h"
#import "RNNElementHorizontalTransition.h"
#import "RNNElementVerticalTransition.h"
#import "RNNHorizontalTranslationTransition.h"
#import "RNNElementFinder.h"
#import "RNNTransition.h"
#import "RNNVerticalRotationTransition.h"
#import "RNNVerticalTranslationTransition.h"

@implementation RNNElementAnimator {
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

- (NSMutableArray<id<RNNDisplayLinkAnimation>> *)createAnimations:
    (TransitionOptions *)transitionOptions {
    NSMutableArray *animations = [NSMutableArray new];
    if (transitionOptions.alpha.hasAnimation) {
        [animations
            addObject:[[RNNElementAlphaTransition alloc] initWithView:self.view
                                                 transitionDetails:transitionOptions.alpha]];
    }

    if (transitionOptions.x.hasAnimation) {
        [animations
            addObject:[[RNNElementHorizontalTransition alloc] initWithView:self.view
                                                      transitionDetails:transitionOptions.x]];
    }

    if (transitionOptions.y.hasAnimation) {
        [animations addObject:[[RNNElementVerticalTransition alloc] initWithView:self.view
                                                            transitionDetails:transitionOptions.y]];
    }

    if (transitionOptions.translationX.hasAnimation) {
        [animations addObject:[[RNNHorizontalTranslationTransition alloc]
                                       initWithView:self.view
                                  transitionDetails:transitionOptions.translationX]];
    }

    if (transitionOptions.translationY.hasAnimation) {
        [animations addObject:[[RNNVerticalTranslationTransition alloc]
                                       initWithView:self.view
                                  transitionDetails:transitionOptions.translationY]];
    }

    if (transitionOptions.rotationY.hasAnimation) {
        [animations addObject:[[RNNVerticalRotationTransition alloc]
                                       initWithView:self.view
                                  transitionDetails:transitionOptions.rotationY]];
    }

    return animations;
}

@end
