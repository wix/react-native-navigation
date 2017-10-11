
#import "RNNTabBarController.h"

@implementation RNNTabBarController

#if !(TARGET_OS_TV)
- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.selectedViewController.supportedInterfaceOrientations;
}
#endif

@end
