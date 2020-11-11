#import "BottomTabsTogetherAttacher.h"
#import "RNNBottomTabsController.h"

@implementation BottomTabsTogetherAttacher

- (void)attach:(RNNBottomTabsController *)bottomTabsController {
    dispatch_group_t ready = dispatch_group_create();

    for (UIViewController *vc in bottomTabsController.pendingChildViewControllers) {
        dispatch_group_enter(ready);
        [vc setReactViewReadyCallback:^{
          dispatch_group_leave(ready);
        }];
        [vc render];
    }

    dispatch_notify(ready, dispatch_get_main_queue(), ^{
      [bottomTabsController readyForPresentation];
    });
}

@end
