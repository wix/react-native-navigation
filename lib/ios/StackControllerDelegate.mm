#import "StackControllerDelegate.h"
#import "ScreenAnimationController.h"
#import "ScreenReversedAnimationController.h"
#import "UIViewController+LayoutProtocol.h"

@implementation StackControllerDelegate {
#ifdef RCT_NEW_ARCH_ENABLED
  RNNEventEmitter *_eventEmitter;
#else
    RNNEventEmitter *_eventEmitter;
#endif
    UIViewController *_presentedViewController;
    BOOL _isPopping;
}

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter {
#else
- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter {
#endif
    self = [super init];
    _eventEmitter = eventEmitter;
    return self;
}

- (void)navigationController:(UINavigationController *)navigationController
      willShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    if (_presentedViewController &&
        ![navigationController.viewControllers containsObject:_presentedViewController]) {
        _isPopping = YES;
    }
}

- (void)navigationController:(UINavigationController *)navigationController
       didShowViewController:(UIViewController *)viewController
                    animated:(BOOL)animated {
    if (_presentedViewController &&
        ![navigationController.viewControllers containsObject:_presentedViewController]) {
        [_presentedViewController screenPopped];
        _isPopping = NO;
    }

    _presentedViewController = viewController;
}

- (BOOL)navigationController:(UINavigationController *)navigationController
               shouldPopItem:(BOOL)shouldPopItem {
    if (@available(iOS 13.0, *)) {
        return shouldPopItem;
    } else {
        if (_isPopping) {
            return YES;
        } else if (shouldPopItem) {
            [navigationController popViewControllerAnimated:YES];
            _isPopping = NO;
            return YES;
        } else {
            return NO;
        }
    }
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
#ifdef RCT_NEW_ARCH_ENABLED
    return [[ScreenAnimationController alloc]
            initWithContentTransition:screenTransition.content
            elementTransitions:screenTransition.elementTransitions
            sharedElementTransitions:screenTransition.sharedElementTransitions
            duration:screenTransition.maxDuration
            host:_eventEmitter.host];
#else
		return [[ScreenAnimationController alloc]
            initWithContentTransition:screenTransition.content
                   elementTransitions:screenTransition.elementTransitions
             sharedElementTransitions:screenTransition.sharedElementTransitions
                             duration:screenTransition.maxDuration
                               bridge:_eventEmitter.bridge];
#endif
    } else if (operation == UINavigationControllerOperationPop &&
               fromVCOptionsWithDefault.animations.pop.hasCustomAnimation) {
        RNNScreenTransition *screenTransition = fromVCOptionsWithDefault.animations.pop;
#ifdef RCT_NEW_ARCH_ENABLED
    return [[ScreenReversedAnimationController alloc]
      initWithContentTransition:screenTransition.content
           elementTransitions:screenTransition.elementTransitions
       sharedElementTransitions:screenTransition.sharedElementTransitions
               duration:screenTransition.maxDuration
                 host:_eventEmitter.host];
#else
        return [[ScreenReversedAnimationController alloc]
            initWithContentTransition:screenTransition.content
                   elementTransitions:screenTransition.elementTransitions
             sharedElementTransitions:screenTransition.sharedElementTransitions
                             duration:screenTransition.maxDuration
                               bridge:_eventEmitter.bridge];
#endif
    } else {
        return nil;
    }

    return nil;
}

@end
