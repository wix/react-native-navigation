#import "UIViewController+SideMenuController.h"


@implementation UIViewController (SideMenuController)

- (RNNSideMenuController *)sideMenuController {
	UIViewController* vc = self;
	while (vc) {
		if ([vc isKindOfClass:[RNNSideMenuController class]]) {
			return (RNNSideMenuController *)vc;
		}
		
		vc = vc.parentViewController;
	}
	
	return nil;
}

@end
