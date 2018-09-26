#import "RNNViewControllerPresenter.h"

@implementation RNNViewControllerPresenter

- (void)presentOn:(UIViewController *)viewController {
	[self.options applyOn:viewController];
	
	if ([self.delegate respondsToSelector:@selector(optionsUpdated)]) {
		[self.delegate optionsUpdated];
	}
}

@end
