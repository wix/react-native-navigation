#import "RNNStatusBarOptions.h"
#import "UIViewController+RNNOptions.h"

@implementation RNNStatusBarOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.blur) {
		[viewController rnn_setStatusBarBlur:[self.blur boolValue]];
	}
	
	if (self.style) {
		[viewController rnn_setStatusBarStyle:self.style animated:[self.animate boolValue]];
	}
}

@end
