#import "RNNBackButtonOptions.h"
#import "UIImage+tint.h"

@implementation RNNBackButtonOptions

- (void)applyOn:(UIViewController *)viewController {
  	UIBarButtonItem *backItem = [[UIBarButtonItem alloc] init];
  
	if (self.icon) {
		backItem.image = self.tintedIcon;
	}
  
	if (self.color) {
	  	[backItem setTintColor:[RCTConvert UIColor:self.color]];
	}
  
	if (self.title) {
	  	backItem.title = self.title;
	}
  
	if (self.showTitle && ![self.showTitle boolValue]) {
	  	self.title = @"";
	}
  
  	[self setBackItem:backItem onViewController:viewController];
	
	if (self.visible) {
		viewController.navigationItem.hidesBackButton = ![self.visible boolValue];
	}
}

- (void)setBackItem:(UIBarButtonItem *)backItem onViewController:(UIViewController *)viewController {
	UINavigationController* nvc = viewController.navigationController;
	if (nvc.viewControllers.count >= 2) {
		UIViewController* lastViewControllerInStack = nvc.viewControllers[nvc.viewControllers.count - 2];
	  	backItem.title = self.title ? self.title : lastViewControllerInStack.navigationItem.title;
		lastViewControllerInStack.navigationItem.backBarButtonItem = backItem;
	}
}

- (UIImage *)tintedIcon {
	UIImage *image = [RCTConvert UIImage:self.icon];
	if (self.color) {
		return [[image withTintColor:[RCTConvert UIColor:self.color]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
	}
	
	return image;
}

@end
