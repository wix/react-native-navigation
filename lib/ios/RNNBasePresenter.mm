#import "RNNBasePresenter.h"
#import "DotIndicatorOptions.h"
#import "RNNReactComponentRegistry.h"
#import "RNNTabBarItemCreator.h"
#import "UIViewController+LayoutProtocol.h"
#import "UIViewController+RNNOptions.h"

@implementation RNNBasePresenter {
    BOOL _prefersHomeIndicatorAutoHidden;
}

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    _defaultOptions = defaultOptions;
    return self;
}

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry
                           defaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [self initWithDefaultOptions:defaultOptions];
    _componentRegistry = componentRegistry;
    return self;
}

- (void)bindViewController:(UIViewController *)boundViewController {
    self.boundComponentId = boundViewController.layoutInfo.componentId;
    _boundViewController = boundViewController;
    RNNNavigationOptions *withDefault =
        (RNNNavigationOptions *)[self.boundViewController.resolveOptions
            withDefault:self.defaultOptions];
    _prefersHomeIndicatorAutoHidden = [withDefault.layout.autoHideHomeIndicator withDefault:NO];
}

- (void)componentWillAppear {
}

- (void)componentDidAppear {
}

- (void)componentDidDisappear {
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
    UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = [initialOptions withDefault:[self defaultOptions]];

    if (@available(iOS 13.0, *)) {
        viewController.modalInPresentation = ![withDefault.modal.swipeToDismiss withDefault:YES];
    }

    if (withDefault.window.backgroundColor.hasValue) {
        UIApplication.sharedApplication.delegate.window.backgroundColor =
            withDefault.window.backgroundColor.get;
    }
}

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options {
}

- (void)applyOptions:(RNNNavigationOptions *)options {
    UIViewController *viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = [options withDefault:[self defaultOptions]];
    if (withDefault.layout.insets.hasValue) {
        viewController.topMostViewController.additionalSafeAreaInsets =
            UIEdgeInsetsMake([withDefault.layout.insets.top withDefault:0],
                             [withDefault.layout.insets.left withDefault:0],
                             [withDefault.layout.insets.bottom withDefault:0],
                             [withDefault.layout.insets.right withDefault:0]);
    }
}

- (void)mergeOptions:(RNNNavigationOptions *)mergeOptions
     resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    RNNNavigationOptions *withDefault = (RNNNavigationOptions *)[[resolvedOptions
        withDefault:_defaultOptions] mergeOptions:mergeOptions];
    if (@available(iOS 13.0, *)) {
        if (withDefault.modal.swipeToDismiss.hasValue)
            self.boundViewController.modalInPresentation = !withDefault.modal.swipeToDismiss.get;
    }

    if (mergeOptions.window.backgroundColor.hasValue) {
        UIApplication.sharedApplication.delegate.window.backgroundColor =
            withDefault.window.backgroundColor.get;
    }

    if (mergeOptions.statusBar.visible.hasValue) {
        [self.boundViewController setNeedsStatusBarAppearanceUpdate];
    }

    if (mergeOptions.layout.autoHideHomeIndicator.hasValue &&
        mergeOptions.layout.autoHideHomeIndicator.get != _prefersHomeIndicatorAutoHidden) {
        _prefersHomeIndicatorAutoHidden = mergeOptions.layout.autoHideHomeIndicator.get;
        [self.boundViewController setNeedsUpdateOfHomeIndicatorAutoHidden];
    }

    if (mergeOptions.layout.insets.hasValue) {
        self.boundViewController.topMostViewController.additionalSafeAreaInsets =
            UIEdgeInsetsMake([withDefault.layout.insets.top withDefault:0],
                             [withDefault.layout.insets.left withDefault:0],
                             [withDefault.layout.insets.bottom withDefault:0],
                             [withDefault.layout.insets.right withDefault:0]);
    }
}

- (void)renderComponents:(RNNNavigationOptions *)options
                 perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    if (readyBlock) {
        readyBlock();
        readyBlock = nil;
    }
}

- (void)viewDidLayoutSubviews {
}

- (UIStatusBarStyle)getStatusBarStyle {
    RNNStatusBarOptions *statusBarOptions = [self resolveStatusBarOptions];
    NSString *statusBarStyle = [statusBarOptions.style withDefault:@"default"];
    if ([statusBarStyle isEqualToString:@"light"]) {
        return UIStatusBarStyleLightContent;
    } else if (@available(iOS 13.0, *)) {
        if ([statusBarStyle isEqualToString:@"dark"]) {
            return UIStatusBarStyleDarkContent;
        }
    }
    return UIStatusBarStyleDefault;
}

- (BOOL)getStatusBarVisibility {
    RNNStatusBarOptions *statusBarOptions = [self resolveStatusBarOptions];
    if (statusBarOptions.visible.hasValue) {
        return ![statusBarOptions.visible get];
    } else if ([statusBarOptions.hideWithTopBar withDefault:NO]) {
        return self.boundViewController.stack.isNavigationBarHidden;
    }
    return NO;
}

- (RNNStatusBarOptions *)resolveStatusBarOptions {
    RNNNavigationOptions *options = self.boundViewController.options.copy;
    [options.statusBar
        mergeOptions:self.boundViewController.getCurrentChild.presenter.resolveStatusBarOptions];
    return [[options withDefault:self.defaultOptions] statusBar];
}

- (UINavigationItem *)currentNavigationItem {
    return self.boundViewController.getCurrentChild.navigationItem;
}

- (UIInterfaceOrientationMask)getOrientation {
    return [self.boundViewController.resolveOptions withDefault:self.defaultOptions]
        .layout.supportedOrientations;
}

- (BOOL)hidesBottomBarWhenPushed {
    RNNNavigationOptions *withDefault =
        (RNNNavigationOptions *)[self.boundViewController.topMostViewController.resolveOptions
            withDefault:self.defaultOptions];
    return ![withDefault.bottomTabs.visible withDefault:YES];
}

- (BOOL)prefersHomeIndicatorAutoHidden {
    return _prefersHomeIndicatorAutoHidden;
}

@end
