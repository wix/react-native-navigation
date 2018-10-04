#import "RNNNavigationControllerPresenter.h"

@implementation RNNNavigationControllerPresenter

- (void)present:(RNNNavigationOptions *)options onViewControllerDidLoad:(UINavigationController *)navigationController {
	[options applyOnNavigationController:navigationController];
}

- (void)present:(RNNNavigationOptions *)options onViewControllerWillAppear:(UINavigationController *)navigationController {
	[options applyOnNavigationController:navigationController];
}

@end
