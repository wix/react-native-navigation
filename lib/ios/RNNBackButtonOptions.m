#import "RNNBackButtonOptions.h"
#import "UIImage+tint.h"

@implementation RNNBackButtonOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.visible) {
		viewController.navigationItem.hidesBackButton = ![self.visible boolValue];
	}
	
	[self applyOnNavigationController:viewController.navigationController];
}

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	if (self.showTitle && ![self.showTitle boolValue]) {
		self.title = @"";
	}
	
	if (self.icon) {
		UIImage *image = self.tintedIcon;
		[navigationController.navigationBar setBackIndicatorImage:[UIImage new]];
		[navigationController.navigationBar setBackIndicatorTransitionMaskImage:[UIImage new]];
		
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithImage:image style:UIBarButtonItemStylePlain target:nil action:nil];
		[self setBackItem:backItem onNavigationController:navigationController];
	} else if (self.title) {
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithTitle:self.title
																	 style:UIBarButtonItemStylePlain
																	target:nil
																	action:nil];
		
		[self setBackItem:backItem onNavigationController:navigationController];
		
	}
}

- (void)setBackItem:(UIBarButtonItem *)backItem onNavigationController:(UINavigationController *)navigationController {
	if (navigationController.viewControllers.count >= 2) {
		UIViewController* lastViewControllerInStack = navigationController.viewControllers[navigationController.viewControllers.count - 2];
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
