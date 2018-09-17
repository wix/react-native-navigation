#import "RNNNavigationControllerPresenter.h"

@implementation RNNNavigationControllerPresenter

- (void)presentOn:(UINavigationController *)navigationController {
	[self.options applyOnNavigationController:navigationController];
}

@end
