#import "ElementTransitionsCreator.h"
#import "RNNElementFinder.h"
#import "UIViewController+LayoutProtocol.h"

@implementation ElementTransitionsCreator

+ (NSArray<DisplayLinkAnimatorDelegate> *)
           create:(NSArray<ElementTransitionOptions *> *)elementTransitions
           fromVC:(UIViewController *)fromVC
             toVC:(UIViewController *)toVC
    containerView:(UIView *)containerView {
    NSMutableArray<DisplayLinkAnimatorDelegate> *transitions =
        [NSMutableArray<DisplayLinkAnimatorDelegate> new];
    for (ElementTransitionOptions *transitionOptions in elementTransitions) {
        UIView *element = [self findElementById:transitionOptions.elementId
                                         fromVC:fromVC
                                           toVC:toVC];
        ElementAnimator *elementAnimator =
            [[ElementAnimator alloc] initWithTransitionOptions:transitionOptions
                                                          view:element
                                                 containerView:containerView];
        [transitions addObject:elementAnimator];
    }

    return transitions;
}

+ (id<DisplayLinkAnimatorDelegate>)createTransition:(ElementTransitionOptions *)transitionOptions
                                               view:(UIView *)view
                                      containerView:(UIView *)containerView {
    ElementAnimator *elementAnimator =
        [[ElementAnimator alloc] initWithTransitionOptions:transitionOptions
                                                      view:view
                                             containerView:containerView];

    return elementAnimator;
}

+ (UIView *)findElementById:(NSString *)elementId
                     fromVC:(UIViewController *)fromVC
                       toVC:(UIViewController *)toVC {
    UIView *viewInSourceView = [RNNElementFinder findElementForId:elementId
                                                           inView:fromVC.reactView];
    if (viewInSourceView) {
        return viewInSourceView;
    }

    UIView *viewInDestinationView = [RNNElementFinder findElementForId:elementId
                                                                inView:toVC.reactView];
    if (viewInDestinationView) {
        return viewInDestinationView;
    }

    return nil;
}

@end
