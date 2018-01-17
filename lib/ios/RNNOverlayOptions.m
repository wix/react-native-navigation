#import "RNNOverlayOptions.h"
#import <React/RCTRootView.h>

@implementation RNNOverlayOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.interceptTouches) {
		RCTRootView* rootView = (RCTRootView*)viewController.view;
		rootView.passThroughTouches = ![self.interceptTouches boolValue];
	}
}

@end
