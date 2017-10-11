
#import "RNNNavigationController.h"

@implementation RNNNavigationController

#if !(TARGET_OS_TV)
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}
#endif

@end
