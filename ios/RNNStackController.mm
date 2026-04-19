#import "RNNStackController.h"
#import "RNNComponentViewController.h"
#import "StackControllerDelegate.h"
#import "UIViewController+Utils.h"

@implementation RNNStackController {
    UIViewController *_presentedViewController;
    StackControllerDelegate *_stackDelegate;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                           creator:(id<RNNComponentViewCreator>)creator
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                         presenter:(RNNBasePresenter *)presenter
                      eventEmitter:(RNNEventEmitter *)eventEmitter
              childViewControllers:(NSArray *)childViewControllers {
    self = [super initWithLayoutInfo:layoutInfo
                             creator:creator
                             options:options
                      defaultOptions:defaultOptions
                           presenter:presenter
                        eventEmitter:eventEmitter
                childViewControllers:childViewControllers];
    _stackDelegate = [[StackControllerDelegate alloc] initWithEventEmitter:self.eventEmitter];
    self.delegate = _stackDelegate;
    self.navigationBar.prefersLargeTitles = YES;
    return self;
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [self.presenter applyOptionsOnViewDidLayoutSubviews:self.resolveOptions];
}

- (void)mergeChildOptions:(RNNNavigationOptions *)options child:(UIViewController *)child {
    if (child.isLastInStack) {
        [self.presenter mergeOptions:options resolvedOptions:self.resolveOptions];
    }
    [self.parentViewController mergeChildOptions:options child:child];
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
    [self prepareForPop];
    [self snapshotTopViewIfNeeded:animated];

    UIViewController *poppedVC = [super popViewControllerAnimated:animated];
    [self teardownPoppedControllers:poppedVC ? @[ poppedVC ] : @[]];
    return poppedVC;
}

- (NSArray<UIViewController *> *)popToViewController:(UIViewController *)viewController
                                             animated:(BOOL)animated {
    [self snapshotTopViewIfNeeded:animated];

    NSArray<UIViewController *> *poppedVCs =
        [super popToViewController:viewController animated:animated];
    [self teardownPoppedControllers:poppedVCs];
    return poppedVCs;
}

- (NSArray<UIViewController *> *)popToRootViewControllerAnimated:(BOOL)animated {
    [self snapshotTopViewIfNeeded:animated];

    NSArray<UIViewController *> *poppedVCs =
        [super popToRootViewControllerAnimated:animated];
    [self teardownPoppedControllers:poppedVCs];
    return poppedVCs;
}

#pragma mark - React view teardown

- (void)snapshotTopViewIfNeeded:(BOOL)animated {
    if (!animated) return;
    UIViewController *topVC = self.topViewController;
    if (!topVC.isViewLoaded || !topVC.view.window) return;

    UIView *snapshot = [topVC.view snapshotViewAfterScreenUpdates:NO];
    if (snapshot) {
        snapshot.frame = topVC.view.bounds;
        snapshot.autoresizingMask =
            UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        [topVC.view addSubview:snapshot];
    }
}

- (void)teardownPoppedControllers:(NSArray<UIViewController *> *)poppedVCs {
    for (UIViewController *vc in poppedVCs) {
        if ([vc isKindOfClass:[RNNComponentViewController class]]) {
            [[(RNNComponentViewController *)vc reactView] componentDidDisappear];
        }
        [vc destroyReactView];
    }
}

- (BOOL)navigationBar:(UINavigationBar *)navigationBar shouldPopItem:(UINavigationItem *)item {
    BOOL shouldPopItem = [self.presenter shouldPopItem:item options:self.getCurrentChild.options];
    if (!shouldPopItem) {
        [self.eventEmitter
            sendOnNavigationButtonPressed:self.getCurrentChild.layoutInfo.componentId
                                 buttonId:[self.getCurrentChild.options.topBar.backButton.identifier
                                              withDefault:@"RNN.back"]];
    }

    return [_stackDelegate navigationController:self shouldPopItem:shouldPopItem];
}

- (void)prepareForPop {
    if (self.viewControllers.count > 1) {
        UIViewController *controller = self.viewControllers[self.viewControllers.count - 2];
        if ([controller isKindOfClass:[RNNComponentViewController class]]) {
            RNNComponentViewController *rnnController = (RNNComponentViewController *)controller;
            [self.presenter applyOptionsBeforePopping:rnnController.resolveOptions];
        }
    }
}

- (UIViewController *)childViewControllerForStatusBarStyle {
    return self.topViewController;
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
