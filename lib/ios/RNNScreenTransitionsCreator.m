#import "RNNScreenTransitionsCreator.h"
#import "ContentTransitionCreator.h"
#import "DisplayLinkAnimatorDelegate.h"
#import "ElementTransitionsCreator.h"
#import "SharedElementTransitionsCreator.h"

@implementation RNNScreenTransitionsCreator

+ (NSArray *)createTransitionsFromVC:(UIViewController *)fromVC
                                toVC:(UIViewController *)toVC
                       containerView:(UIView *)containerView
                   contentTransition:(TransitionOptions *)contentTransitionOptions
                  elementTransitions:
                      (NSArray<ElementTransitionOptions *> *)elementTransitionsOptions
            sharedElementTransitions:
                (NSArray<SharedElementTransitionOptions *> *)sharedElementTransitionsOptions
                            reversed:(BOOL)reversed {
    NSArray *elementTransitions = [ElementTransitionsCreator create:elementTransitionsOptions
                                                             fromVC:fromVC
                                                               toVC:toVC
                                                      containerView:containerView];
    NSArray *sharedElementTransitions =
        [SharedElementTransitionsCreator create:sharedElementTransitionsOptions
                                         fromVC:fromVC
                                           toVC:toVC
                                  containerView:containerView];
    id<DisplayLinkAnimatorDelegate> contentTransition =
        [self createContentTransitionFromVC:fromVC
                                       toVC:toVC
                              containerView:containerView
                          contentTransition:contentTransitionOptions
                                   reversed:reversed];

    return [[[NSArray arrayWithObject:contentTransition]
        arrayByAddingObjectsFromArray:elementTransitions]
        arrayByAddingObjectsFromArray:sharedElementTransitions];
}

+ (id<DisplayLinkAnimatorDelegate>)createContentTransitionFromVC:(UIViewController *)fromVC
                                                            toVC:(UIViewController *)toVC
                                                   containerView:(UIView *)containerView
                                               contentTransition:
                                                   (TransitionOptions *)contentTransition
                                                        reversed:(BOOL)reversed {
    UIView *contentView = reversed ? fromVC.view : toVC.view;
    return [ContentTransitionCreator createTransition:contentTransition
                                                 view:contentView
                                               fromVC:fromVC
                                                 toVC:toVC
                                        containerView:containerView
                                             reversed:reversed];
}

@end
