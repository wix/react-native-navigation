#import "RNNOverlayOptions.h"
#import <React/RCTRootView.h>

@implementation RNNOverlayOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.interceptTouchOutside) {
		RCTRootView* rootView = (RCTRootView*)viewController.view;
		rootView.passThroughTouches = ![self.interceptTouchOutside boolValue];
	}
}

@end
