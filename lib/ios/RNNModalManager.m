#import "RNNModalManager.h"
#import "RNNComponentViewController.h"
#import "RNNConvert.h"
#import "ScreenAnimationController.h"
#import "ScreenReversedAnimationController.h"
#import "UIViewController+LayoutProtocol.h"
#import "ViewAnimationOptions.h"

@interface RNNModalManager ()
@property(nonatomic, strong) ScreenAnimationController *showModalTransitionDelegate;
@property(nonatomic, strong) ScreenAnimationController *dismissModalTransitionDelegate;
@end

@implementation RNNModalManager {
    NSMutableArray *_pendingModalIdsToDismiss;
    NSMutableArray *_presentedModals;
    RCTBridge *_bridge;
    RNNModalManagerEventHandler *_eventHandler;
}

- (instancetype)init {
    self = [super init];
    _pendingModalIdsToDismiss = [[NSMutableArray alloc] init];
    _presentedModals = [[NSMutableArray alloc] init];
    return self;
}

- (instancetype)initWithBridge:(RCTBridge *)bridge
                  eventHandler:(RNNModalManagerEventHandler *)eventHandler {
    self = [self init];
    _bridge = bridge;
    _eventHandler = eventHandler;
    return self;
}

- (void)showModal:(UIViewController<RNNLayoutProtocol> *)viewController
         animated:(BOOL)animated
       completion:(RNNTransitionWithComponentIdCompletionBlock)completion {
    if (!viewController) {
        @throw [NSException exceptionWithName:@"ShowUnknownModal"
                                       reason:@"showModal called with nil viewController"
                                     userInfo:nil];
    }

    UIViewController *topVC = [self topPresentedVC];

    viewController.modalPresentationStyle = [RNNConvert
        UIModalPresentationStyle:[viewController.resolveOptionsWithDefault.modalPresentationStyle
                                     withDefault:@"default"]];
    viewController.modalTransitionStyle = [RNNConvert
        UIModalTransitionStyle:[viewController.resolveOptionsWithDefault.modalTransitionStyle
                                   withDefault:@"coverVertical"]];

    if (viewController.presentationController) {
        viewController.presentationController.delegate = self;
    }

    if (viewController.resolveOptionsWithDefault.animations.showModal.hasAnimation) {
        ViewAnimationOptions *viewAnimationOptions =
            viewController.resolveOptionsWithDefault.animations.showModal;
        _showModalTransitionDelegate = [[ScreenAnimationController alloc]
            initWithContentTransition:viewAnimationOptions
                   elementTransitions:viewAnimationOptions.elementTransitions
             sharedElementTransitions:viewAnimationOptions.sharedElementTransitions
                             duration:viewAnimationOptions.maxDuration
                               bridge:_bridge];

        viewController.transitioningDelegate = _showModalTransitionDelegate;
    }

    [topVC presentViewController:viewController
                        animated:animated
                      completion:^{
                        if (completion) {
                            completion(viewController.layoutInfo.componentId);
                        }

                        [self->_presentedModals addObject:[viewController topMostViewController]];
                      }];
}

- (void)dismissModal:(UIViewController *)viewController
          completion:(RNNTransitionCompletionBlock)completion {
    if (viewController) {
        [_pendingModalIdsToDismiss addObject:viewController];
        [self removePendingNextModalIfOnTop:completion];
    }
}

- (void)dismissAllModalsAnimated:(BOOL)animated completion:(void (^__nullable)(void))completion {
    UIViewController *root = [self rootViewController];
    if (root.presentedViewController) {
        ViewAnimationOptions *dismissModalOptions =
            root.presentedViewController.resolveOptionsWithDefault.animations.dismissModal;
        if (dismissModalOptions.hasAnimation) {
            _dismissModalTransitionDelegate = [[ScreenAnimationController alloc]
                initWithContentTransition:dismissModalOptions
                       elementTransitions:dismissModalOptions.elementTransitions
                 sharedElementTransitions:dismissModalOptions.sharedElementTransitions
                                 duration:dismissModalOptions.maxDuration
                                   bridge:_bridge];

            root.presentedViewController.transitioningDelegate = _dismissModalTransitionDelegate;
        }

        [root dismissViewControllerAnimated:animated completion:completion];
        [_eventHandler dismissedMultipleModals:_presentedModals];
        [_pendingModalIdsToDismiss removeAllObjects];
        [_presentedModals removeAllObjects];
    } else if (completion)
        completion();
}

- (void)dismissAllModalsSynchronosly {
    if (_presentedModals.count) {
        dispatch_semaphore_t sem = dispatch_semaphore_create(0);
        [self dismissAllModalsAnimated:NO
                            completion:^{
                              dispatch_semaphore_signal(sem);
                            }];

        while (dispatch_semaphore_wait(sem, DISPATCH_TIME_NOW)) {
            [[NSRunLoop currentRunLoop] runMode:NSDefaultRunLoopMode
                                     beforeDate:[NSDate dateWithTimeIntervalSinceNow:0]];
        }
    }
}

#pragma mark - private

- (void)removePendingNextModalIfOnTop:(RNNTransitionCompletionBlock)completion {
    UIViewController<RNNLayoutProtocol> *modalToDismiss = [_pendingModalIdsToDismiss lastObject];
    RNNNavigationOptions *optionsWithDefault = modalToDismiss.resolveOptionsWithDefault;

    if (!modalToDismiss) {
        return;
    }

    UIViewController *topPresentedVC = [self topPresentedVC];

    if (optionsWithDefault.animations.dismissModal.hasAnimation) {
        ViewAnimationOptions *viewAnimationOptions =
            modalToDismiss.resolveOptionsWithDefault.animations.dismissModal;
        _dismissModalTransitionDelegate = [[ScreenReversedAnimationController alloc]
            initWithContentTransition:viewAnimationOptions
                   elementTransitions:viewAnimationOptions.elementTransitions
             sharedElementTransitions:viewAnimationOptions.sharedElementTransitions
                             duration:viewAnimationOptions.maxDuration
                               bridge:_bridge];

        [self topViewControllerParent:modalToDismiss].transitioningDelegate =
            _dismissModalTransitionDelegate;
    }

    if (modalToDismiss == topPresentedVC ||
        [[topPresentedVC childViewControllers] containsObject:modalToDismiss]) {
        [self dismissSearchController:modalToDismiss];
        [modalToDismiss
            dismissViewControllerAnimated:[optionsWithDefault.animations.dismissModal.exit.enable
                                              withDefault:YES]
                               completion:^{
                                 [self->_pendingModalIdsToDismiss removeObject:modalToDismiss];
                                 if (modalToDismiss.view) {
                                     [self dismissedModal:modalToDismiss];
                                 }

                                 if (completion) {
                                     completion();
                                 }

                                 [self removePendingNextModalIfOnTop:nil];
                               }];
    } else {
        [modalToDismiss.view removeFromSuperview];
        modalToDismiss.view = nil;
        modalToDismiss.getCurrentChild.resolveOptions.animations.dismissModal.exit.enable =
            [[Bool alloc] initWithBOOL:NO];
        [self dismissedModal:modalToDismiss];

        if (completion) {
            completion();
        }
    }
}

- (void)dismissSearchController:(UIViewController *)modalToDismiss {
    if ([modalToDismiss.presentedViewController.class isSubclassOfClass:UISearchController.class]) {
        [modalToDismiss.presentedViewController dismissViewControllerAnimated:NO completion:nil];
    }
}

- (void)dismissedModal:(UIViewController *)viewController {
    [_presentedModals removeObject:[viewController topMostViewController]];
    [_eventHandler dismissedModal:viewController.presentedComponentViewController];
}

- (void)presentationControllerDidDismiss:(UIPresentationController *)presentationController {
    [_presentedModals removeObject:presentationController.presentedViewController];
    [_eventHandler dismissedModal:presentationController.presentedViewController
                                      .presentedComponentViewController];
}

- (void)presentationControllerDidAttemptToDismiss:
    (UIPresentationController *)presentationController {
    [_eventHandler attemptedToDismissModal:presentationController.presentedViewController
                                               .presentedComponentViewController];
}

- (UIViewController *)rootViewController {
    return UIApplication.sharedApplication.delegate.window.rootViewController;
}

- (UIViewController *)topPresentedVC {
    UIViewController *root = [self rootViewController];
    while (root.presentedViewController) {
        root = root.presentedViewController;
    }
    return root;
}

- (UIViewController *)topPresentedVCLeaf {
    id root = [self topPresentedVC];
    return [root topViewController] ? [root topViewController] : root;
}

- (UIViewController *)topViewControllerParent:(UIViewController *)viewController {
    UIViewController *topParent = viewController;
    while (topParent.parentViewController) {
        topParent = topParent.parentViewController;
    }

    return topParent;
}

@end
