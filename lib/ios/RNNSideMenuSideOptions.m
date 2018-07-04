#import "RNNSideMenuSideOptions.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuSideOptions

- (void)applyOnSide:(MMDrawerSide)side viewController:(UIViewController *)viewController {
	RNNSideMenuController* sideMenuController = (RNNSideMenuController*)UIApplication.sharedApplication.delegate.window.rootViewController;
	if (sideMenuController && [sideMenuController isKindOfClass:[RNNSideMenuController class]]) {
		if (self.enabled) {
			switch (side) {
				case MMDrawerSideRight:
					sideMenuController.sideMenu.rightSideEnabled = [self.enabled boolValue];
					break;
				case MMDrawerSideLeft:
					sideMenuController.sideMenu.leftSideEnabled = [self.enabled boolValue];
				default:
					break;
			}
		}
		
		if (self.visible) {
			if (self.visible.boolValue) {
				[sideMenuController showSideMenu:side animated:YES];
			} else {
				[sideMenuController hideSideMenu:side animated:YES];
			}
		}

		if (self.width) {
			switch (side) {
				case MMDrawerSideRight:
					sideMenuController.sideMenu.maximumRightDrawerWidth = self.width.floatValue;
					break;
				case MMDrawerSideLeft:
					sideMenuController.sideMenu.maximumLeftDrawerWidth = self.width.floatValue;
				default:
					break;
			}
		}
	}
	
	[self resetOptions];
}

- (void)resetOptions {
	self.visible = nil;
	self.enabled = nil;
}

@end
