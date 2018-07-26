#import "RNNOverlayManager.h"
#import "RNNOverlayWindow.h"

@implementation RNNOverlayManager

- (instancetype)init {
	self = [super init];
	_overlayWindows = [[NSMutableArray alloc] init];
	return self;
}

#pragma mark - public

- (void)showOverlay:(UIViewController *)viewController {
	UIWindow* overlayWindow = [[RNNOverlayWindow alloc] initWithFrame:[[UIScreen mainScreen] bounds]];
	[_overlayWindows addObject:overlayWindow];
	[overlayWindow setWindowLevel:UIWindowLevelNormal];
	[overlayWindow setRootViewController:viewController];
	[overlayWindow setHidden:NO];
}

- (void)dismissOverlay:(UIViewController*)viewController {
	UIWindow* overlayWindow = [self findWindowByRootViewController:viewController];
	[self detachOverlayWindow:overlayWindow];
}

#pragma mark - private

- (void)detachOverlayWindow:(UIWindow *)overlayWindow {
	[overlayWindow setHidden:YES];
	[overlayWindow setRootViewController:nil];
	[_overlayWindows removeObject:overlayWindow];
}

- (UIWindow *)findWindowByRootViewController:(UIViewController *)viewController {
	for (UIWindow* window in _overlayWindows) {
		if ([window.rootViewController isEqual:viewController]) {
			return window;
		}
	}
	
	return nil;
}

@end
