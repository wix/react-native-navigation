#import "RNNBottomTabsOnSwitchToTabAttacher.h"

@implementation RNNBottomTabsOnSwitchToTabAttacher

- (void)attach:(UITabBarController *)bottomTabsController {
    [bottomTabsController.selectedViewController setReactViewReadyCallback:^{
      [bottomTabsController readyForPresentation];
    }];

    [bottomTabsController.selectedViewController render];
}

@end
