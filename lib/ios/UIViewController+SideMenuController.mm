#import "UIViewController+SideMenuController.h"

@implementation UIViewController (SideMenuController)

- (RNNSideMenuViewController *)sideMenuController {
    UIViewController *vc = self;
    while (vc) {
        if ([vc isKindOfClass:[RNNSideMenuViewController class]]) {
            return (RNNSideMenuViewController *)vc;
        }

        vc = vc.parentViewController;
    }

    return nil;
}

@end
