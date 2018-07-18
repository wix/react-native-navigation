#import "RNNBackgroundOptions.h"

@implementation RNNBackgroundOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.color) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.color];
		viewController.navigationController.navigationBar.barTintColor = backgroundColor;
	} else {
		viewController.navigationController.navigationBar.barTintColor = nil;
	}
}

@end
