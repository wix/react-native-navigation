#import "RNNStackController.h"
#import "RNNComponentViewController.h"

@implementation RNNStackController {
    UIViewController* _presentedViewController;
}

- (instancetype)init {
    self = [super init];
    self.delegate = self;
    return self;
}

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super setDefaultOptions:defaultOptions];
	[self.presenter setDefaultOptions:defaultOptions];
}

- (void)viewDidLayoutSubviews {
	[super viewDidLayoutSubviews];
	[self.presenter applyOptionsOnViewDidLayoutSubviews:self.resolveOptions];
}

- (UINavigationController *)navigationController {
	return self;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return [_presenter getStatusBarStyle:self.resolveOptions];
}

- (UIModalPresentationStyle)modalPresentationStyle {
	return self.getCurrentChild.modalPresentationStyle;
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
    [self prepareForPop];
	return [super popViewControllerAnimated:animated];
}

- (void)navigationController:(UINavigationController *)navigationController didShowViewController:(UIViewController *)viewController animated:(BOOL)animated {
    if ([self.viewControllers indexOfObject:_presentedViewController] < 0) {
        [self sendScreenPoppedEvent:_presentedViewController];
    }
    
    _presentedViewController = viewController;
}

- (void)sendScreenPoppedEvent:(UIViewController *)poppedScreen {
    [self.eventEmitter sendScreenPoppedEvent:poppedScreen.layoutInfo.componentId];
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

@end
