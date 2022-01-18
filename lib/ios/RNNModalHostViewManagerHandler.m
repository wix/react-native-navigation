#import "RNNModalHostViewManagerHandler.h"

@implementation RNNModalHostViewManagerHandler {
    RNNModalManager *_modalManager;
}

- (instancetype)initWithModalManager:(RNNModalManager *)modalManager {
    self = [super init];
    _modalManager = modalManager;
    return self;
}

- (void)connectModalHostViewManager:(RCTModalHostViewManager *)modalHostViewManager {
    modalHostViewManager.presentationBlock =
        ^(UIViewController *reactViewController, UIViewController *viewController, BOOL animated,
          dispatch_block_t completionBlock) {
          if (reactViewController.presentedViewController != viewController &&
              [self->_modalManager topPresentedVC] != viewController) {
              [self->_modalManager showModal:viewController
                                    animated:animated
                                  completion:^(NSString *_Nonnull componentId) {
                                    if (completionBlock)
                                        completionBlock();
                                  }];
          }
        };

    modalHostViewManager.dismissalBlock =
        ^(UIViewController *reactViewController, UIViewController *viewController, BOOL animated,
          dispatch_block_t completionBlock) {
          [self->_modalManager dismissModal:viewController
                                   animated:animated
                                 completion:^{
                                   if (completionBlock)
                                       completionBlock();
                                 }];
        };
}

@end
