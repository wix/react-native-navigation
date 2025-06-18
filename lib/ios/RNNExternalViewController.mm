#import "RNNExternalViewController.h"
#import "AnimationObserver.h"

@implementation RNNExternalViewController {
    UIViewController *_boundViewController;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                      eventEmitter:(RNNEventEmitter *)eventEmitter
                         presenter:(RNNComponentPresenter *)presenter
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions
                    viewController:(UIViewController *)viewController {
    _boundViewController = viewController;
    self = [super initWithLayoutInfo:layoutInfo
                     rootViewCreator:nil
                        eventEmitter:eventEmitter
                           presenter:presenter
                             options:options
                      defaultOptions:defaultOptions];
    [self bindViewController:viewController];
    return self;
}

- (void)bindViewController:(UIViewController *)viewController {
    _boundViewController = viewController;
    [self addChildViewController:viewController];
    [self.view addSubview:viewController.view];
    [viewController didMoveToParentViewController:self];
}

- (UINavigationItem *)navigationItem {
    return _boundViewController.navigationItem;
}

- (void)loadView {
    self.view = [[UIView alloc] initWithFrame:UIScreen.mainScreen.bounds];
}

- (void)render {
    [self readyForPresentation];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.eventEmitter sendComponentWillAppear:self.layoutInfo.componentId
                                 componentName:self.layoutInfo.name
                                 componentType:ComponentTypeScreen];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [[AnimationObserver sharedObserver] endAnimation];
    [self.eventEmitter sendComponentDidAppear:self.layoutInfo.componentId
                                componentName:self.layoutInfo.name
                                componentType:ComponentTypeScreen];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.eventEmitter sendComponentDidDisappear:self.layoutInfo.componentId
                                   componentName:self.layoutInfo.name
                                   componentType:ComponentTypeScreen];
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
