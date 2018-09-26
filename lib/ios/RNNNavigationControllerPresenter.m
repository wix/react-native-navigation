#import "RNNNavigationControllerPresenter.h"

@implementation RNNNavigationControllerPresenter

- (void)present:(RNNOptions *)options on:(UINavigationController *)navigationController {
	[options applyOnNavigationController:navigationController];
}

@end
