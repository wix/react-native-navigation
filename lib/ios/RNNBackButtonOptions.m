#import "RNNBackButtonOptions.h"
#import "UIImage+tint.h"

@implementation RNNBackButtonOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.icon) {
		UIImage *image = self.tintedIcon;
		[viewController.navigationController.navigationBar setBackIndicatorImage:[UIImage new]];
		[viewController.navigationController.navigationBar setBackIndicatorTransitionMaskImage:[UIImage new]];
		
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithImage:image style:UIBarButtonItemStylePlain target:nil action:nil];
		[self setBackItem:backItem onViewController:viewController];
	} else if (self.title) {
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithTitle:self.title
																	 style:UIBarButtonItemStylePlain
																	target:nil
																	action:nil];
		
		[self setBackItem:backItem onViewController:viewController];
	}
	
	if (self.visible) {
		viewController.navigationItem.hidesBackButton = ![self.visible boolValue];
	}
	
	if (self.showTitle && ![self.showTitle boolValue]) {
		self.title = @"";
	}
}

- (void)setBackItem:(UIBarButtonItem *)backItem onViewController:(UIViewController *)viewController {
	UINavigationController* nvc = viewController.navigationController;
	if (nvc.viewControllers.count >= 2) {
		UIViewController* lastViewControllerInStack = nvc.viewControllers[nvc.viewControllers.count - 2];
		lastViewControllerInStack.navigationItem.backBarButtonItem = backItem;
	}
}

- (UIImage *)tintedIcon {
	UIImage *image = self.icon ? [RCTConvert UIImage:self.icon] : nil;
	if (self.color) {
		return [[image withTintColor:[RCTConvert UIColor:self.color]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
	}
	
	return image;
}

@end
