#import "RNNScreenTransitionsCreator.h"
#import "DisplayLinkAnimatorDelegate.h"
#import "ElementTransitionsCreator.h"
#import "SharedElementTransitionsCreator.h"

@implementation RNNScreenTransitionsCreator

+ (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView
                   contentTransition:(RNNEnterExitAnimation *)contentTransitionOptions
                  elementTransitions:
                      (NSArray<ElementTransitionOptions *> *)elementTransitionsOptions
            sharedElementTransitions:
                (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitionsOptions {
    NSArray *elementTransitions = [ElementTransitionsCreator create:elementTransitionsOptions
                                                             fromVC:fromVC
                                                               toVC:toVC
                                                      containerView:containerView];
    NSArray *sharedElementTransitions =
        [SharedElementTransitionsCreator create:sharedElementTransitionsOptions
                                         fromVC:fromVC
                                           toVC:toVC
                                  containerView:containerView];

    id<DisplayLinkAnimatorDelegate> enterTransition =
        [ElementTransitionsCreator createTransition:contentTransitionOptions.enter
                                               view:toVC.view
                                      containerView:containerView];

    id<DisplayLinkAnimatorDelegate> exitTransition;
    if (contentTransitionOptions.exit.hasAnimation) {
        exitTransition = [ElementTransitionsCreator createTransition:contentTransitionOptions.exit
                                                                view:fromVC.view
                                                       containerView:containerView];
    }

    return [[[NSArray arrayWithObjects:enterTransition, exitTransition, nil]
        arrayByAddingObjectsFromArray:elementTransitions]
        arrayByAddingObjectsFromArray:sharedElementTransitions];
}

@end
