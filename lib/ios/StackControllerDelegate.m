#import "StackControllerDelegate.h"
#import "ReversedTransitionDelegate.h"
#import "TransitionDelegate.h"
#import "UIViewController+LayoutProtocol.h"

@implementation StackControllerDelegate {
    RNNEventEmitter *_eventEmitter;
    UIViewController *_presentedViewController;
}

- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter {
    self = [super init];
    _eventEmitter = eventEmitter;
    return self;
}

- (void)navigationController:(UINavigationController *)navigationController
       didShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    if (_presentedViewController &&
        ![navigationController.viewControllers containsObject:_presentedViewController]) {
        [_presentedViewController screenPopped];
    }

    _presentedViewController = viewController;
}

- (id<UIViewControllerAnimatedTransitioning>)
               navigationController:(UINavigationController *)navigationController
    animationControllerForOperation:(UINavigationControllerOperation)operation
                 fromViewController:(UIViewController *)fromVC
                   toViewController:(UIViewController *)toVC {
    RNNNavigationOptions *toVCOptionsWithDefault = toVC.resolveOptionsWithDefault;
    RNNNavigationOptions *fromVCOptionsWithDefault = fromVC.resolveOptionsWithDefault;
    if (operation == UINavigationControllerOperationPush &&
        toVCOptionsWithDefault.animations.push.hasCustomAnimation) {
        RNNScreenTransition *screenTransition = toVCOptionsWithDefault.animations.push;
        return [[TransitionDelegate alloc]
            initWithContentTransition:screenTransition.content
                   elementTransitions:screenTransition.elementTransitions
             sharedElementTransitions:screenTransition.sharedElementTransitions
                             duration:screenTransition.maxDuration
                               bridge:_eventEmitter.bridge];
    } else if (operation == UINavigationControllerOperationPop &&
               fromVCOptionsWithDefault.animations.pop.hasCustomAnimation) {
        RNNScreenTransition *screenTransition = fromVCOptionsWithDefault.animations.pop;
        return [[ReversedTransitionDelegate alloc]
            initWithContentTransition:screenTransition.content
                   elementTransitions:screenTransition.elementTransitions
             sharedElementTransitions:screenTransition.sharedElementTransitions
                             duration:screenTransition.maxDuration
                               bridge:_eventEmitter.bridge];
    } else {
        return nil;
    }

    return nil;
}

@end
