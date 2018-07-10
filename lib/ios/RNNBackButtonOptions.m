#import "RNNBackButtonOptions.h"

@implementation RNNBackButtonOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.icon) {
		UIImage *image = self.icon ? [RCTConvert UIImage:self.icon] : nil;
		[viewController.navigationController.navigationBar setBackIndicatorImage:image];
		[viewController.navigationController.navigationBar setBackIndicatorTransitionMaskImage:image];
	}
	
	if (self.visible) {
		viewController.navigationItem.hidesBackButton = ![self.visible boolValue];
	}
	
	if (self.showTitle && ![self.showTitle boolValue]) {
		self.title = @"";
	}
	
	if (self.title) {
		UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithTitle:self.title
																	 style:UIBarButtonItemStylePlain
																	target:nil
																	action:nil];
		
		viewController.navigationItem.backBarButtonItem = backItem;
	}
	
	if (self.color) {
		UIColor* buttonColor = [RCTConvert UIColor:self.color];
		viewController.navigationController.navigationBar.tintColor = buttonColor;
	} else {
		viewController.navigationController.navigationBar.tintColor = nil;
	}
}

@end
