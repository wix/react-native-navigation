#import "RNNModalManager.h"
#import "RNNComponentViewController.h"
#import "RNNConvert.h"
#import "ReversedTransitionDelegate.h"
#import "TransitionDelegate.h"
#import "UIViewController+LayoutProtocol.h"
#import "ViewAnimationOptions.h"

@interface RNNModalManager ()
@property(nonatomic, strong) TransitionDelegate *showModalTransitionDelegate;
@property(nonatomic, strong) TransitionDelegate *dismissModalTransitionDelegate;
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
                                     getWithDefaultValue:@"default"]];
    viewController.modalTransitionStyle = [RNNConvert
        UIModalTransitionStyle:[viewController.resolveOptionsWithDefault.modalTransitionStyle
                                   getWithDefaultValue:@"coverVertical"]];

    if (viewController.presentationController) {
        viewController.presentationController.delegate = self;
    }

    if (viewController.resolveOptionsWithDefault.animations.showModal.hasAnimation) {
        ViewAnimationOptions *viewAnimationOptions =
            viewController.resolveOptionsWithDefault.animations.showModal;
        _showModalTransitionDelegate = [[TransitionDelegate alloc]
            initWithContentTransition:viewAnimationOptions
                   elementTransitions:viewAnimationOptions.elementTransitions
             sharedElementTransitions:viewAnimationOptions.sharedElementTransitions
                             duration:viewAnimationOptions.maxDuration
                               bridge:_bridge];

        viewController.transitioningDelegate = _showModalTransitionDelegate;
        viewController.modalPresentationStyle = UIModalPresentationCustom;
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
    UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
    [root dismissViewControllerAnimated:animated completion:completion];
    [_eventHandler dismissedMultipleModals:_presentedModals];
    [_pendingModalIdsToDismiss removeAllObjects];
    [_presentedModals removeAllObjects];
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
        _dismissModalTransitionDelegate = [[ReversedTransitionDelegate alloc]
            initWithContentTransition:viewAnimationOptions
                   elementTransitions:viewAnimationOptions.elementTransitions
             sharedElementTransitions:viewAnimationOptions.sharedElementTransitions
                             duration:viewAnimationOptions.maxDuration
                               bridge:_bridge];

        modalToDismiss.modalPresentationStyle = UIModalPresentationCustom;
        [self topViewControllerParent:modalToDismiss].transitioningDelegate =
            _dismissModalTransitionDelegate;
    }

    if (modalToDismiss == topPresentedVC ||
        [[topPresentedVC childViewControllers] containsObject:modalToDismiss]) {
        [modalToDismiss
            dismissViewControllerAnimated:[optionsWithDefault.animations.dismissModal.enable
                                              getWithDefaultValue:YES]
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
        modalToDismiss.getCurrentChild.resolveOptions.animations.dismissModal.enable =
            [[Bool alloc] initWithBOOL:NO];
        [self dismissedModal:modalToDismiss];

        if (completion) {
            completion();
        }
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

- (UIViewController *)topPresentedVC {
    UIViewController *root = UIApplication.sharedApplication.delegate.window.rootViewController;
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
