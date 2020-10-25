#import "SharedElementTransitionsCreator.h"
#import "AnimatedViewFactory.h"
#import "BaseAnimator.h"
#import "NSArray+utils.h"
#import "RNNElementFinder.h"
#import "SharedElementAnimator.h"
#import "UIViewController+LayoutProtocol.h"

@implementation SharedElementTransitionsCreator

+ (NSArray<DisplayLinkAnimatorDelegate> *)
           create:(NSArray<SharedElementTransitionOptions *> *)sharedElementTransitions
           fromVC:(UIViewController *)fromVC
             toVC:(UIViewController *)toVC
    containerView:(UIView *)containerView {
    NSMutableArray<DisplayLinkAnimatorDelegate> *transitions =
        [NSMutableArray<DisplayLinkAnimatorDelegate> new];
    for (SharedElementTransitionOptions *transitionOptions in sharedElementTransitions) {
        UIView *fromView =
            [RNNElementFinder findElementForId:transitionOptions.fromId
                                        inView:fromVC.presentedComponentViewController.reactView];
        UIView *toView =
            [RNNElementFinder findElementForId:transitionOptions.toId
                                        inView:toVC.presentedComponentViewController.reactView];
        if (fromView == nil || toView == nil) {
            break;
        }

        SharedElementAnimator *sharedElementAnimator =
            [[SharedElementAnimator alloc] initWithTransitionOptions:transitionOptions
                                                            fromView:fromView
                                                              toView:toView
                                                              fromVC:fromVC
                                                                toVC:toVC
                                                       containerView:containerView];
        [transitions addObject:sharedElementAnimator];
    }

    NSArray<DisplayLinkAnimatorDelegate> *sortedTransitions = [self sortByZIndex:transitions];
    [self addSharedElementViews:sortedTransitions toContainerView:containerView];

    return sortedTransitions;
}

+ (void)addSharedElementViews:(NSArray<BaseAnimator *> *)animators
              toContainerView:(UIView *)containerView {
    for (BaseAnimator *animator in animators) {
        [containerView addSubview:animator.view];
    }
}

+ (NSArray<DisplayLinkAnimatorDelegate> *)sortByZIndex:
    (NSArray<DisplayLinkAnimatorDelegate> *)animators {
    return (NSArray<DisplayLinkAnimatorDelegate> *)[animators
        sortedArrayUsingComparator:^NSComparisonResult(BaseAnimator *a, BaseAnimator *b) {
          id first = [a.view valueForKey:@"reactZIndex"];
          id second = [b.view valueForKey:@"reactZIndex"];
          return [first compare:second];
        }];
}

@end
