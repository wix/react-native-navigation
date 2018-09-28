#import "RNNNavigationControllerPresenter.h"

@implementation RNNNavigationControllerPresenter

- (void)present:(RNNNavigationOptions *)options onViewControllerDidLoad:(UINavigationController *)navigationController {
	[options applyOnNavigationController:navigationController];

}

@end
