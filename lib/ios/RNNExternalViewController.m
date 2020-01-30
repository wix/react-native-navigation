#import "RNNExternalViewController.h"

@implementation RNNExternalViewController {
    UIViewController* _boundViewController;
}

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo eventEmitter:(RNNEventEmitter *)eventEmitter presenter:(RNNComponentPresenter *)presenter options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions viewController:(UIViewController *)viewController {
    _boundViewController = viewController;
    self = [super initWithLayoutInfo:layoutInfo rootViewCreator:nil eventEmitter:eventEmitter presenter:presenter options:options defaultOptions:defaultOptions];
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

@end
