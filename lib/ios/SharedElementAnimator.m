#import "SharedElementAnimator.h"
#import "AnimatedViewFactory.h"
#import "BaseAnimator.h"
#import "NSArray+utils.h"
#import "RNNElementFinder.h"
#import "SharedElementTransition.h"
#import "UIViewController+LayoutProtocol.h"

@implementation SharedElementAnimator {
    NSArray<SharedElementTransitionOptions *> *_sharedElementTransitions;
    NSArray *_transitions;
    UIViewController *_fromVC;
    UIViewController *_toVC;
    UIView *_containerView;
}

- (instancetype)initWithTransitions:
                    (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitions
                             fromVC:(UIViewController *)fromVC
                               toVC:(UIViewController *)toVC
                      containerView:(UIView *)containerView {
    self = [super init];
    _sharedElementTransitions = sharedElementTransitions;
    _fromVC = fromVC;
    _toVC = toVC;
    _containerView = containerView;

    return self;
}

- (NSArray<DisplayLinkAnimatorDelegate> *)create {
    NSMutableArray<DisplayLinkAnimatorDelegate> *transitions =
        [NSMutableArray<DisplayLinkAnimatorDelegate> new];
    for (SharedElementTransitionOptions *transitionOptions in _sharedElementTransitions) {
        UIView *fromView =
            [RNNElementFinder findElementForId:transitionOptions.fromId
                                        inView:_fromVC.presentedComponentViewController.reactView];
        UIView *toView =
            [RNNElementFinder findElementForId:transitionOptions.toId
                                        inView:_toVC.presentedComponentViewController.reactView];
        if (fromView == nil || toView == nil) {
            break;
        }

        SharedElementTransition *sharedElementAnimator =
            [[SharedElementTransition alloc] initWithTransitionOptions:transitionOptions
                                                              fromView:fromView
                                                                toView:toView
                                                         containerView:_containerView];
        [transitions addObject:sharedElementAnimator];
    }

    NSArray<DisplayLinkAnimatorDelegate> *sortedTransitions = [self sortByZIndex:transitions];
    [self addSharedElementViews:sortedTransitions toContainerView:_containerView];
    _transitions = transitions;

    return sortedTransitions;
}

- (void)animationEnded {
    for (SharedElementTransition *transition in _transitions.reverseObjectEnumerator) {
        [transition.view reset];
    }
}

- (void)addSharedElementViews:(NSArray<BaseAnimator *> *)animators
              toContainerView:(UIView *)containerView {
    for (BaseAnimator *animator in animators) {
        [containerView addSubview:animator.view];
    }
}

- (NSArray<DisplayLinkAnimatorDelegate> *)sortByZIndex:
    (NSArray<DisplayLinkAnimatorDelegate> *)animators {
    return (NSArray<DisplayLinkAnimatorDelegate> *)[animators
        sortedArrayUsingComparator:^NSComparisonResult(BaseAnimator *a, BaseAnimator *b) {
          id first = [a.view valueForKey:@"reactZIndex"];
          id second = [b.view valueForKey:@"reactZIndex"];
          return [first compare:second];
        }];
}

@end
