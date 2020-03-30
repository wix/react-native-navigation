#import "RNNSplitViewController.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNSplitViewController

- (void)setViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers {
    [super setViewControllers:viewControllers];
    UIViewController<UISplitViewControllerDelegate>* masterViewController = viewControllers[0];
    self.delegate = masterViewController;
}

- (UIViewController *)getCurrentChild {
	return self.viewControllers[0];
}

# pragma mark - UIViewController overrides

- (void)willMoveToParentViewController:(UIViewController *)parent {
    if (parent) {
        [self.presenter applyOptionsOnWillMoveToParentViewController:self.resolveOptions];
        [self onChildAddToParent:self options:self.resolveOptions];
    }
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    return [self.presenter getStatusBarStyle:self.resolveOptions];
}

- (BOOL)prefersStatusBarHidden {
    return [self.presenter statusBarVisibile:self.navigationController resolvedOptions:self.resolveOptions];
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
    return [self.presenter getOrientation:self.resolveOptions];
}

@end
