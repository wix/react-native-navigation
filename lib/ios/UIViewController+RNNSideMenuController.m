#import "UIViewController+RNNSideMenuController.h"

@implementation UIViewController (RNNSideMenuController)

- (RNNSideMenuController *)sideMenuController {
    UIViewController *vc = self;
    while (vc) {
        if ([vc isKindOfClass:[RNNSideMenuController class]]) {
            return (RNNSideMenuController *)vc;
        }

        vc = vc.parentViewController;
    }

    return nil;
}

@end
