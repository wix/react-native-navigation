
#import "InteractivePopGestureDelegate.h"

@implementation InteractivePopGestureDelegate

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
	if (self.navigationController.viewControllers.count < 2) {
		return NO;
	} else if (self.navigationController.navigationBarHidden) {
		return YES;
	} else if (!self.navigationController.navigationBarHidden && self.originalDelegate == nil) {
		return YES;
	} else {
		return [self.originalDelegate gestureRecognizer:gestureRecognizer shouldReceiveTouch:touch];
	}
}

@end
