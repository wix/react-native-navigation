#import "RNNBackgroundOptions.h"

@implementation RNNBackgroundOptions

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	if (self.color && ![self.color isKindOfClass:[NSNull class]]) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.color];
		navigationController.navigationBar.barTintColor = backgroundColor;
	}
}

@end
