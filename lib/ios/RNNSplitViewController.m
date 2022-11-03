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

#if !TARGET_OS_TV
- (UIStatusBarStyle)preferredStatusBarStyle {
    return [self.presenter getStatusBarStyle];
}
#endif

- (BOOL)prefersStatusBarHidden {
    return [self.presenter getStatusBarVisibility];
}

#if !TARGET_OS_TV
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.presenter getOrientation];
}
#endif

- (BOOL)hidesBottomBarWhenPushed {
    return [self.presenter hidesBottomBarWhenPushed];
}

@end
