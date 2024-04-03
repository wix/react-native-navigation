#import "RNNBottomTabsAfterInitialTabAttacher.h"
#import "UITabBarController+RNNUtils.h"

@implementation RNNBottomTabsAfterInitialTabAttacher

- (void)attach:(UITabBarController *)bottomTabsController {
    [bottomTabsController.selectedViewController setReactViewReadyCallback:^{
      [bottomTabsController readyForPresentation];
      for (UIViewController *viewController in bottomTabsController.deselectedViewControllers) {
          [viewController render];
      }
    }];

    [bottomTabsController.selectedViewController render];
}

@end
