#import "RNNOverlayManager.h"
#import "RNNOverlayWindow.h"

@implementation RNNOverlayManager

- (instancetype)init {
	self = [super init];
	_overlayWindows = [[NSMutableArray alloc] init];
	return self;
}

#pragma mark - public

- (void)showOverlayWindow:(RNNOverlayWindow *)overlayWindow {
	overlayWindow.previousWindow = [UIApplication sharedApplication].keyWindow;
	[_overlayWindows addObject:overlayWindow];
	overlayWindow.rootViewController.view.backgroundColor = [UIColor clearColor];
	[overlayWindow setWindowLevel:UIWindowLevelNormal];
	[overlayWindow setHidden: NO];
}

- (void)showOverlayWindowAsKeyWindow:(RNNOverlayWindow *)overlayWindow {
	[self showOverlayWindow:overlayWindow];
	[overlayWindow makeKeyWindow];
}

- (void)dismissOverlay:(UIViewController*)viewController {
	RNNOverlayWindow* overlayWindow = [self findWindowByRootViewController:viewController];
	[self detachOverlayWindow:overlayWindow];
}

- (void)dismissAllOverlays {
    for (RNNOverlayWindow* overlayWindow  in [_overlayWindows reverseObjectEnumerator]) {
        [self detachOverlayWindow:overlayWindow];
    }
}

#pragma mark - private

- (void)detachOverlayWindow:(RNNOverlayWindow *)overlayWindow {
    [overlayWindow.previousWindow makeKeyWindow];
	[overlayWindow setHidden:YES];
	[overlayWindow setRootViewController:nil];
	[_overlayWindows removeObject:overlayWindow];
}

- (RNNOverlayWindow *)findWindowByRootViewController:(UIViewController *)viewController {
	for (RNNOverlayWindow* window in _overlayWindows) {
		if ([window.rootViewController isEqual:viewController]) {
			return window;
		}
	}
	
	return nil;
}

@end
