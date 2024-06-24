#import "BottomTabsAfterInitialTabAttacher.h"
#import "UITabBarController+RNNUtils.h"

@implementation BottomTabsAfterInitialTabAttacher

- (void)attach:(UITabBarController *)bottomTabsController {
    [bottomTabsController.selectedViewController setReactViewReadyCallback:^{
      [bottomTabsController readyForPresentation];
      for (UIViewController *viewController in bottomTabsController.deselectedViewControllers) {
        dispatch_async(dispatch_get_main_queue(), ^{
          [viewController render];
        });
      }
    }];

    [bottomTabsController.selectedViewController render];
}

@end
