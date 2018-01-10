#import "RNNOverlayManager.h"

@implementation RNNOverlayManager {
	UIViewController* _currentOverlay;
}

- (void)showOverlay:(UIViewController *)viewController completion:(RNNTransitionCompletionBlock)completion {
	_currentOverlay = viewController;
	[[[UIApplication sharedApplication] keyWindow] addSubview:viewController.view];
	completion();
}

- (void)dismissOverlay:(RNNTransitionCompletionBlock)completion {
	[_currentOverlay.view removeFromSuperview];
	completion();
}

@end
