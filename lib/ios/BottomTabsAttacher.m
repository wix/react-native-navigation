#import "BottomTabsAttacher.h"
#import "UITabBarController+RNNUtils.h"
#import "RCTConvert+RNNOptions.h"
#import "UIViewController+LayoutProtocol.h"

@implementation BottomTabsAttacher {
    BottomTabsAttachMode _attachMode;
}

- (instancetype)initWithOptions:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    _attachMode = [RCTConvert BottomTabsAttachMode:[[options withDefault:defaultOptions].bottomTabs.tabsAttachMode getWithDefaultValue:@"together"]];
    return self;
}

- (void)attach:(UITabBarController *)bottomTabsController {
    switch (_attachMode) {
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
