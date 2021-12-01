#import "RNNSplitViewController.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNSplitViewController

- (void)setViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers {
    [super setViewControllers:viewControllers];
    UIViewController<UISplitViewControllerDelegate> *masterViewController = viewControllers[0];
    self.delegate = masterViewController;
}

- (UIViewController *)getCurrentChild {
    return self.viewControllers[0];
}

#pragma mark - UIViewController overrides

- (UIStatusBarStyle)preferredStatusBarStyle {
    return [self.presenter getStatusBarStyle];
}

- (BOOL)prefersStatusBarHidden {
    return [self.presenter getStatusBarVisibility];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.presenter getOrientation];
}

- (BOOL)hidesBottomBarWhenPushed {
    return [self.presenter hidesBottomBarWhenPushed];
}

@end
