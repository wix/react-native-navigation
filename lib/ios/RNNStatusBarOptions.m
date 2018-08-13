#import "RNNStatusBarOptions.h"
#define kStatusBarAnimationDuration 0.35

@implementation RNNStatusBarOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.blur) {
		UIView* curBlurView = [viewController.view viewWithTag:BLUR_STATUS_TAG];
		if ([self.blur boolValue]) {
			if (!curBlurView) {
				UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
				blur.frame = [[UIApplication sharedApplication] statusBarFrame];
				blur.tag = BLUR_STATUS_TAG;
				[viewController.view insertSubview:blur atIndex:0];
			}
		} else {
			if (curBlurView) {
				[curBlurView removeFromSuperview];
			}
		}
	}
	
	if (self.style || self.visible) {
		if (self.animate) {
			[UIView animateWithDuration:[self statusBarAnimationDuration] animations:^{
				[viewController setNeedsStatusBarAppearanceUpdate];
			}];
		} else {
			[viewController setNeedsStatusBarAppearanceUpdate];
		}
	}
}

- (CGFloat)statusBarAnimationDuration {
	return [self.animate boolValue] ? kStatusBarAnimationDuration : CGFLOAT_MIN;
}

@end
