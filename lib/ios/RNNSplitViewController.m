#import "RNNSplitViewController.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNSplitViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo creator:(id<RNNComponentViewCreator>)creator options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions presenter:(RNNBasePresenter *)presenter eventEmitter:(RNNEventEmitter *)eventEmitter childViewControllers:(NSArray *)childViewControllers {
    self = [super initWithLayoutInfo:layoutInfo creator:creator options:options defaultOptions:defaultOptions presenter:presenter eventEmitter:eventEmitter childViewControllers:childViewControllers];
    [self loadChildren];
    return self;
}

- (void)setViewControllers:(NSArray<__kindof UIViewController *> *)viewControllers {
    [super setViewControllers:viewControllers];
    UIViewController<UISplitViewControllerDelegate>* masterViewController = viewControllers[0];
    self.delegate = masterViewController;
}

- (UIViewController *)getCurrentChild {
    return self.viewControllers.count > 0 ? self.viewControllers[0] : self.children[0];
}

# pragma mark - UIViewController overrides

- (void)willMoveToParentViewController:(UIViewController *)parent {
    [self.presenter willMoveToParentViewController:parent];
}

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
