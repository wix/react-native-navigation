#import "RNNBackgroundOptions.h"

@implementation RNNBackgroundOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.color && ![self.color isKindOfClass:[NSNull class]]) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.color];
		viewController.navigationController.navigationBar.barTintColor = backgroundColor;
	}
}

@end
