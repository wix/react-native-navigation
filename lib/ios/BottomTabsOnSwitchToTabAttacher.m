#import "BottomTabsOnSwitchToTabAttacher.h"

@implementation BottomTabsOnSwitchToTabAttacher

- (void)attach:(UITabBarController *)bottomTabsController {
    [bottomTabsController.selectedViewController setReactViewReadyCallback:^{
        [bottomTabsController readyForPresentation];
    }];
    
    [bottomTabsController.selectedViewController render];
}

@end
