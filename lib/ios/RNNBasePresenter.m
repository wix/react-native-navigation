#import "RNNBasePresenter.h"

@implementation RNNBasePresenter

- (void)present:(RNNNavigationOptions *)options onViewControllerWillAppear:(UIViewController *)viewController {
	[options applyOn:viewController];
}

- (void)present:(RNNNavigationOptions *)options onViewControllerDidLoad:(UIViewController *)viewController {
	[options applyOn:viewController];
}

@end
