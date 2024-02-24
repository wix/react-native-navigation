#import "RNNScreenTransitionsCreator.h"
#import "RNNDisplayLinkAnimatorDelegateProtocol.h"
#import "RNNElementTransitionsCreator.h"

@implementation RNNScreenTransitionsCreator

+ (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView
                   contentTransition:(RNNEnterExitAnimation *)contentTransitionOptions
                  elementTransitions:
                      (NSArray<RNNElementTransitionOptions *> *)elementTransitionsOptions {
    NSArray *elementTransitions = [RNNElementTransitionsCreator create:elementTransitionsOptions
                                                             fromVC:fromVC
                                                               toVC:toVC
                                                      containerView:containerView];
    id<RNNDisplayLinkAnimatorDelegateProtocol> enterTransition =
        [RNNElementTransitionsCreator createTransition:contentTransitionOptions.enter
                                               view:toVC.view
                                      containerView:containerView];

    id<RNNDisplayLinkAnimatorDelegateProtocol> exitTransition;
    if (contentTransitionOptions.exit.hasAnimation) {
        exitTransition = [RNNElementTransitionsCreator createTransition:contentTransitionOptions.exit
                                                                view:fromVC.view
                                                       containerView:containerView];
    }

    return [[NSArray arrayWithObjects:enterTransition, exitTransition, nil]
        arrayByAddingObjectsFromArray:elementTransitions];
}

@end
