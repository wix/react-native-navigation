
#import "InteractivePopGestureDelegate.h"

@implementation InteractivePopGestureDelegate

- (instancetype)init {
    self = [super init];
    _enabled = YES;
    return self;
}
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch {
	if (self.navigationController.viewControllers.count < 2 || !_enabled) {
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
