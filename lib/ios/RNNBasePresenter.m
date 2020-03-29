#import "RNNBasePresenter.h"
#import "UIViewController+RNNOptions.h"
#import "RNNTabBarItemCreator.h"
#import "RNNReactComponentRegistry.h"
#import "UIViewController+LayoutProtocol.h"
#import "DotIndicatorOptions.h"

@implementation RNNBasePresenter

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    _defaultOptions = defaultOptions;
    return self;
}

- (instancetype)initWithComponentRegistry:(RNNReactComponentRegistry *)componentRegistry defaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [self initWithDefaultOptions:defaultOptions];
    _componentRegistry = componentRegistry;
    return self;
}

- (void)bindViewController:(UIViewController *)boundViewController {
    self.boundComponentId = boundViewController.layoutInfo.componentId;
    _boundViewController = boundViewController;
}

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    _defaultOptions = defaultOptions;
}

- (void)componentDidAppear {
    
}

- (void)componentDidDisappear {
    
}

- (void)applyOptionsOnInit:(RNNNavigationOptions *)initialOptions {
    UIViewController* viewController = self.boundViewController;
    RNNNavigationOptions *withDefault = [initialOptions withDefault:[self defaultOptions]];
    
    if (@available(iOS 13.0, *)) {
        viewController.modalInPresentation = ![withDefault.modal.swipeToDismiss getWithDefaultValue:YES];
    }
	
	UIApplication.sharedApplication.delegate.window.backgroundColor = [withDefault.window.backgroundColor getWithDefaultValue:nil];
}

- (void)applyOptionsOnViewDidLayoutSubviews:(RNNNavigationOptions *)options {

}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {

}

- (void)applyOptions:(RNNNavigationOptions *)options {

}

- (void)mergeOptions:(RNNNavigationOptions *)options resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    RNNNavigationOptions* withDefault = (RNNNavigationOptions *) [[resolvedOptions withDefault:_defaultOptions] overrideOptions:options];
	
	if (options.window.backgroundColor.hasValue) {
		UIApplication.sharedApplication.delegate.window.backgroundColor = withDefault.window.backgroundColor.get;
	}
}

- (void)renderComponents:(RNNNavigationOptions *)options perform:(RNNReactViewReadyCompletionBlock)readyBlock {
    if (readyBlock) {
        readyBlock();
        readyBlock = nil;
    }
}

- (void)viewDidLayoutSubviews {

}

- (UIStatusBarStyle)getStatusBarStyle:(RNNNavigationOptions *)resolvedOptions {
    RNNNavigationOptions *withDefault = [resolvedOptions withDefault:[self defaultOptions]];
    NSString* statusBarStyle = [withDefault.statusBar.style getWithDefaultValue:@"default"];
    if ([statusBarStyle isEqualToString:@"light"]) {
        return UIStatusBarStyleLightContent;
    } else if (@available(iOS 13.0, *)) {
        if ([statusBarStyle isEqualToString:@"dark"]) {
            return UIStatusBarStyleDarkContent;
        } else {
            return UIStatusBarStyleDefault;
        }
    } else {
        return UIStatusBarStyleDefault;
    }
}

- (UINavigationItem *)currentNavigationItem {
    return self.boundViewController.getCurrentChild.navigationItem;
}

- (UIInterfaceOrientationMask)getOrientation:(RNNNavigationOptions *)options {
    return [options withDefault:[self defaultOptions]].layout.supportedOrientations;
}

- (BOOL)statusBarVisibile:(UINavigationController *)stack resolvedOptions:(RNNNavigationOptions *)resolvedOptions {
    RNNNavigationOptions *withDefault = [resolvedOptions withDefault:self.defaultOptions];
    if (withDefault.statusBar.visible.hasValue) {
        return ![withDefault.statusBar.visible get];
    } else if ([withDefault.statusBar.hideWithTopBar getWithDefaultValue:NO]) {
        return stack.isNavigationBarHidden;
    }
    return NO;
}


@end
