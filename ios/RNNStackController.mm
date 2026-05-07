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
    UIViewController *previousTop = self.topViewController;
    UIView *snapshot = [self snapshotTopView:animated];

    UIViewController *poppedVC = [super popViewControllerAnimated:animated];
    if (!poppedVC) {
        [snapshot removeFromSuperview];
        return nil;
    }

    id<UIViewControllerTransitionCoordinator> coordinator = self.transitionCoordinator;
    if (coordinator && coordinator.isInteractive) {
        // Interactive pop (swipe-back): remove snapshot overlay — UIKit shows the live
        // view during the gesture. Skip early teardown so the React view stays alive
        // if the gesture is cancelled. The delegate's didShowViewController handles
        // cleanup once the animation finishes.
        [snapshot removeFromSuperview];
    } else {
        [self teardownPoppedControllers:@[ poppedVC ] previousTop:previousTop];
    }
    return poppedVC;
}

- (NSArray<UIViewController *> *)popToViewController:(UIViewController *)viewController
                                             animated:(BOOL)animated {
    UIViewController *previousTop = self.topViewController;
    UIView *snapshot = [self snapshotTopView:animated];

    NSArray<UIViewController *> *poppedVCs =
        [super popToViewController:viewController animated:animated];
    if (poppedVCs.count > 0) {
        [self teardownPoppedControllers:poppedVCs previousTop:previousTop];
    } else {
        [snapshot removeFromSuperview];
    }
    return poppedVCs;
}

- (NSArray<UIViewController *> *)popToRootViewControllerAnimated:(BOOL)animated {
    UIViewController *previousTop = self.topViewController;
    UIView *snapshot = [self snapshotTopView:animated];

    NSArray<UIViewController *> *poppedVCs =
        [super popToRootViewControllerAnimated:animated];
    if (poppedVCs.count > 0) {
        [self teardownPoppedControllers:poppedVCs previousTop:previousTop];
    } else {
        [snapshot removeFromSuperview];
    }
    return poppedVCs;
}

#pragma mark - React view teardown

- (UIView *)snapshotTopView:(BOOL)animated {
    if (!animated) return nil;
    UIViewController *topVC = self.topViewController;
    if (!topVC.isViewLoaded || !topVC.view.window) return nil;

    UIView *snapshot = [topVC.view snapshotViewAfterScreenUpdates:NO];
    if (snapshot) {
        snapshot.frame = topVC.view.bounds;
        snapshot.autoresizingMask =
            UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
        [topVC.view addSubview:snapshot];
    }
    return snapshot;
}

- (void)teardownPoppedControllers:(NSArray<UIViewController *> *)poppedVCs
                      previousTop:(UIViewController *)previousTop {
    for (UIViewController *vc in poppedVCs) {
        if (vc == previousTop && [vc isKindOfClass:[RNNComponentViewController class]]) {
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
