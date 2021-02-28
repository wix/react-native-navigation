#import "RNNStackController.h"
#import "RNNComponentViewController.h"
#import "RNNNavigationBarDelegateHandler.h"
#import "StackControllerDelegate.h"
#import "UIViewController+Utils.h"

@implementation RNNStackController {
    UIViewController *_presentedViewController;
    StackControllerDelegate *_stackDelegate;
    RNNNavigationBarDelegateHandler *_navigationBarDelegateHandler;
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
    _navigationBarDelegateHandler = RNNNavigationBarDelegateHandler.new;
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
    [_navigationBarDelegateHandler popViewControllerAnimated:animated];
    [self prepareForPop];
    return [super popViewControllerAnimated:animated];
}

- (BOOL)navigationBar:(UINavigationBar *)navigationBar shouldPopItem:(UINavigationItem *)item {
    BOOL shouldPopItem = [self.presenter shouldPopItem:item options:self.getCurrentChild.options];
    if (!shouldPopItem) {
        [self.eventEmitter
            sendOnNavigationButtonPressed:self.getCurrentChild.layoutInfo.componentId
                                 buttonId:[self.getCurrentChild.options.topBar.backButton.identifier
                                              withDefault:@"RNN.back"]];
    }

    return [_navigationBarDelegateHandler navigationController:self shouldPopItem:shouldPopItem];
}

- (void)navigationBar:(UINavigationBar *)navigationBar didPopItem:(UINavigationItem *)item {
    [_navigationBarDelegateHandler navigationBar:navigationBar didPopItem:item];
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
