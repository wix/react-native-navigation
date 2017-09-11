
#import "RNNNavigationController.h"

@implementation RNNNavigationController

- (UIStatusBarStyle)preferredStatusBarStyle {
	return self.viewControllers.lastObject.preferredStatusBarStyle;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

@end
