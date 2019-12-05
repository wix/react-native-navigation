#import "BottomTabsAttacher.h"
#import "UITabBarController+RNNUtils.h"

@implementation BottomTabsAttacher

- (void)attach:(RNNBottomTabsController *)bottomTabsController withMode:(BottomTabsAttachMode)tabsAttachMode {
    switch (tabsAttachMode) {
        case BottomTabsAttachModeTogether: {
            for (UIViewController* childViewController in bottomTabsController.childViewControllers) {
                [childViewController render];
            }
            
            [bottomTabsController readyForPresentation];
        }
            break;
        case BottomTabsAttachModeAfterInitialTab: {
            [bottomTabsController.selectedViewController setReactViewReadyCallback:^{
                [bottomTabsController readyForPresentation];
                for (UIViewController* viewController in bottomTabsController.deselectedViewControllers) {
                    [viewController render];
                }
            }];
            
            [bottomTabsController.selectedViewController render];
        }
            break;
        case BottomTabsAttachModeOnSwitchToTab: {
            [bottomTabsController.selectedViewController setReactViewReadyCallback:^{
                [bottomTabsController readyForPresentation];
            }];
            
            [bottomTabsController.selectedViewController render];
        }
        break;
        default:
            break;
    }
}

@end
