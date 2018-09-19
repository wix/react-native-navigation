#import "RNNBackButtonOptions.h"
#import "UIImage+tint.h"

@implementation RNNBackButtonOptions

- (void)applyOn:(UIViewController *)viewController {
  	UIBarButtonItem *backItem = [[UIBarButtonItem alloc] init];
  
	backItem.image = self.tintedIconIfAvailable;
  
	if (self.color) {
	  	backItem.tintColor = [RCTConvert UIColor:self.color];
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

- (UIImage *)tintedIconIfAvailable {
	if (self.icon) {
		UIImage *image = [RCTConvert UIImage:self.icon];
	  	return self.color
	  		? [[image withTintColor:[RCTConvert UIColor:self.color]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
	  		: image;
	}
  
  	return nil;
}

@end
