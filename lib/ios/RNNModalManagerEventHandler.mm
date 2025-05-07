#import "RNNModalManagerEventHandler.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNModalManagerEventHandler {
#ifdef RCT_NEW_ARCH_ENABLED
    RNNTurboEventEmitter *_eventEmitter;
#else
    RNNEventEmitter *_eventEmitter;
#endif
}

#ifdef RCT_NEW_ARCH_ENABLED
- (instancetype)initWithEventEmitter:(RNNTurboEventEmitter *)eventEmitter {
    self = [super init];
    _eventEmitter = eventEmitter;
    return self;
}
#else
- (instancetype)initWithEventEmitter:(RNNEventEmitter *)eventEmitter {
    self = [super init];
    _eventEmitter = eventEmitter;
    return self;
}
#endif

- (void)dismissedModal:(UIViewController *)viewController {
    [_eventEmitter
        sendModalsDismissedEvent:viewController.topMostViewController.layoutInfo.componentId
         numberOfModalsDismissed:@(1)];
}

- (void)attemptedToDismissModal:(UIViewController *)viewController {
    [_eventEmitter sendModalAttemptedToDismissEvent:viewController.topMostViewController.layoutInfo
                                                        .componentId];
}

- (void)dismissedMultipleModals:(NSArray *)viewControllers {
    if (viewControllers && viewControllers.count) {
        UIViewController *lastViewController =
            [viewControllers.lastObject presentedComponentViewController];
        [_eventEmitter
            sendModalsDismissedEvent:lastViewController.topMostViewController.layoutInfo.componentId
             numberOfModalsDismissed:@(viewControllers.count)];
    }
}

@end
