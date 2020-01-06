#import "RNNExternalViewController.h"

@implementation RNNExternalViewController

- (instancetype)initWithLayoutInfo:(RNNLayoutInfo *)layoutInfo eventEmitter:(RNNEventEmitter *)eventEmitter presenter:(RNNComponentPresenter *)presenter options:(RNNNavigationOptions *)options defaultOptions:(RNNNavigationOptions *)defaultOptions viewController:(UIViewController *)viewController {
	self = [super initWithLayoutInfo:layoutInfo rootViewCreator:nil eventEmitter:eventEmitter presenter:presenter options:options defaultOptions:defaultOptions];
    [self bindViewController:viewController];
	return self;
}

- (void)bindViewController:(UIViewController *)viewController {
    [self addChildViewController:viewController];
    [self.view addSubview:viewController.view];
    [viewController didMoveToParentViewController:self];
}

- (void)loadView {
	self.view = [UIView new];
}

- (void)render {
	[self readyForPresentation];
}

@end
