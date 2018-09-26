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
  UIBarButtonItem *backItem = [[UIBarButtonItem alloc] init];
  	UIImage* tintedIcon = self.tintedIconIfAvailable;
	if (tintedIcon) {
		backItem.image = tintedIcon;
		[navigationController.navigationBar setBackIndicatorImage:[UIImage new]];	
		[navigationController.navigationBar setBackIndicatorTransitionMaskImage:[UIImage new]];
	}
  
	if (self.color) {
	  	backItem.tintColor = [RCTConvert UIColor:self.color];
	}
  
	if (self.showTitle && ![self.showTitle boolValue]) {
	  	self.title = @"";
	}
  
  	[self setBackItem:backItem onNavigationController:navigationController];
}

- (void)setBackItem:(UIBarButtonItem *)backItem onNavigationController:(UINavigationController *)navigationController {
	NSArray *viewControllers = navigationController.viewControllers;
	UIViewController *lastViewControllerInStack = [viewControllers lastObject];
	backItem.title = self.title ? self.title : lastViewControllerInStack.navigationItem.title;
	lastViewControllerInStack.navigationItem.backBarButtonItem = backItem;
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
