#import "RNNComponentViewController.h"
#import "AnimationObserver.h"

@implementation RNNComponentViewController {
    NSArray *_reactViewConstraints;
}

@synthesize previewCallback;

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo
                   rootViewCreator:(id<RNNComponentViewCreator>)creator
                      eventEmitter:(RNNEventEmitter *)eventEmitter
                         presenter:(RNNComponentPresenter *)presenter
                           options:(RNNNavigationOptions *)options
                    defaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super initWithLayoutInfo:layoutInfo
                             creator:creator
                             options:options
                      defaultOptions:defaultOptions
                           presenter:presenter
                        eventEmitter:eventEmitter
                childViewControllers:nil];
    if (@available(iOS 13.0, *)) {
        self.navigationItem.standardAppearance = [UINavigationBarAppearance new];
        self.navigationItem.scrollEdgeAppearance = [UINavigationBarAppearance new];
    }
    return self;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    [self.reactView componentWillAppear];
    [self componentWillAppear];
    [_presenter applyOptions:self.resolveOptions];
    [self.parentViewController onChildWillAppear];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    [[AnimationObserver sharedObserver] endAnimation];
    [self.reactView componentDidAppear];
    [self componentDidAppear];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    // Fix's momentum scroll bug
    // https://github.com/wix/react-native-navigation/issues/4325
    [self.view stopMomentumScrollViews];
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    [self.reactView componentDidDisappear];
    [self componentDidDisappear];
}

- (RNNNavigationOptions *)resolveOptions {
    RNNNavigationOptions *resolvedOptions = self.options.copy;
    UIViewController *parentViewController = self.parentViewController;
    while (parentViewController) {
        resolvedOptions = [resolvedOptions withDefault:parentViewController.options];
        parentViewController = parentViewController.parentViewController;
    }

    return resolvedOptions;
}

- (void)loadView {
    [self renderReactViewIfNeeded];
}

- (void)render {
    if (!self.waitForRender)
        [self readyForPresentation];

    [self renderReactViewIfNeeded];
}

- (void)destroyReactView {
    [self.reactView invalidate];
}

- (void)renderReactViewIfNeeded {
    if (!self.reactView) {
        self.view = [[UIView alloc] initWithFrame:UIScreen.mainScreen.bounds];
        self.reactView = [self.creator createRootView:self.layoutInfo.name
                                           rootViewId:self.layoutInfo.componentId
                                               ofType:RNNComponentTypeComponent
                                  reactViewReadyBlock:^{
                                    [self->_presenter renderComponents:self.resolveOptions
                                                               perform:^{
                                                                 [self readyForPresentation];
                                                               }];
                                  }];
        self.reactView.backgroundColor = UIColor.clearColor;
        self.reactView.translatesAutoresizingMaskIntoConstraints = NO;
        [self.view addSubview:self.reactView];
        [self updateReactViewConstraints];
    } else {
        [self readyForPresentation];
    }
}

- (void)setInterceptTouchOutside:(BOOL)interceptTouchOutside {
    self.reactView.passThroughTouches = !interceptTouchOutside;
}

- (void)viewSafeAreaInsetsDidChange {
    [super viewSafeAreaInsetsDidChange];
    [self updateReactViewConstraints];
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    [self.presenter applyOptionsOnViewDidLayoutSubviews:self.resolveOptions];
}

- (void)updateReactViewConstraints {
    if (self.isViewLoaded && self.reactView) {
        [NSLayoutConstraint deactivateConstraints:_reactViewConstraints];
        _reactViewConstraints = @[
            [self.reactView.topAnchor
                constraintEqualToAnchor:self.shouldDrawBehindTopBar
                                            ? self.view.topAnchor
                                            : self.view.safeAreaLayoutGuide.topAnchor],
            [self.reactView.bottomAnchor
                constraintEqualToAnchor:self.shouldDrawBehindBottomTabs
                                            ? self.view.bottomAnchor
                                            : self.view.safeAreaLayoutGuide.bottomAnchor],
            [self.reactView.leftAnchor constraintEqualToAnchor:self.view.leftAnchor],
            [self.reactView.rightAnchor constraintEqualToAnchor:self.view.rightAnchor]
        ];
        [NSLayoutConstraint activateConstraints:_reactViewConstraints];
    }
}

- (BOOL)shouldDrawBehindBottomTabs {
    return (!self.tabBarController.tabBar || self.tabBarController.tabBar.isHidden ||
            _drawBehindBottomTabs);
}

- (BOOL)shouldDrawBehindTopBar {
    return (!self.navigationController.navigationBar ||
            self.navigationController.navigationBar.isHidden || _drawBehindTopBar);
}

- (void)setDrawBehindTopBar:(BOOL)drawBehindTopBar {
    _drawBehindTopBar = drawBehindTopBar;
    [self updateReactViewConstraints];
}

- (void)setDrawBehindBottomTabs:(BOOL)drawBehindBottomTabs {
    _drawBehindBottomTabs = drawBehindBottomTabs;
    [self updateReactViewConstraints];
}

- (UIViewController *)getCurrentChild {
    return nil;
}

- (void)updateSearchResultsForSearchController:(UISearchController *)searchController {
    [self.eventEmitter sendOnSearchBarUpdated:self.layoutInfo.componentId
                                         text:searchController.searchBar.text
                                    isFocused:searchController.searchBar.isFirstResponder];
}

- (void)screenPopped {
    [_eventEmitter sendScreenPoppedEvent:self.layoutInfo.componentId];
}

- (void)searchBarCancelButtonClicked:(UISearchBar *)searchBar {
    [self.eventEmitter sendOnSearchBarCancelPressed:self.layoutInfo.componentId];
}

- (UIViewController *)previewingContext:(id<UIViewControllerPreviewing>)previewingContext
              viewControllerForLocation:(CGPoint)location {
    return self.previewController;
}

- (void)previewingContext:(id<UIViewControllerPreviewing>)previewingContext
     commitViewController:(UIViewController *)viewControllerToCommit {
    if (self.previewCallback) {
        self.previewCallback(self);
    }
}

- (void)onActionPress:(NSString *)id {
    [_eventEmitter sendOnNavigationButtonPressed:self.layoutInfo.componentId buttonId:id];
}

- (UIPreviewAction *)convertAction:(NSDictionary *)action {
    NSString *actionId = action[@"id"];
    NSString *actionTitle = action[@"title"];
    UIPreviewActionStyle actionStyle = UIPreviewActionStyleDefault;
    if ([action[@"style"] isEqualToString:@"selected"]) {
        actionStyle = UIPreviewActionStyleSelected;
    } else if ([action[@"style"] isEqualToString:@"destructive"]) {
        actionStyle = UIPreviewActionStyleDestructive;
    }

    return [UIPreviewAction actionWithTitle:actionTitle
                                      style:actionStyle
                                    handler:^(UIPreviewAction *_Nonnull action,
                                              UIViewController *_Nonnull previewViewController) {
                                      [self onActionPress:actionId];
                                    }];
}

- (NSArray<id<UIPreviewActionItem>> *)previewActionItems {
    NSMutableArray *actions = [[NSMutableArray alloc] init];
    for (NSDictionary *previewAction in self.resolveOptions.preview.actions) {
        UIPreviewAction *action = [self convertAction:previewAction];
        NSDictionary *actionActions = previewAction[@"actions"];
        if (actionActions.count > 0) {
            NSMutableArray *group = [[NSMutableArray alloc] init];
            for (NSDictionary *previewGroupAction in actionActions) {
                [group addObject:[self convertAction:previewGroupAction]];
            }
            UIPreviewActionGroup *actionGroup =
                [UIPreviewActionGroup actionGroupWithTitle:action.title
                                                     style:UIPreviewActionStyleDefault
                                                   actions:group];
            [actions addObject:actionGroup];
        } else {
            [actions addObject:action];
        }
    }
    return actions;
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

- (BOOL)prefersHomeIndicatorAutoHidden {
    return [self.presenter prefersHomeIndicatorAutoHidden];
}

@end
