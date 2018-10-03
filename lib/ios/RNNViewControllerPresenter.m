#import "RNNViewControllerPresenter.h"

@implementation RNNViewControllerPresenter

- (void)present:(RNNNavigationOptions *)options onViewControllerDidLoad:(UIViewController *)viewController {
	[options applyOn:viewController];
}

@end
