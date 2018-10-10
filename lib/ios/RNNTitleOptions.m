#import "RNNTitleOptions.h"
#import "RNNTitleViewHelper.h"
#import "UIViewController+RNNOptions.h"
#import "UINavigationController+RNNOptions.h"

@implementation RNNTitleOptions

- (void)applyOn:(UIViewController *)viewController {
	if (self.text) {
		[viewController rnn_setNavigationItemTitle:self.text];
	}
	
	[viewController.navigationController rnn_setNavigationBarFontFamily:self.fontFamily fontSize:self.fontSize color:[RCTConvert UIColor:self.color]];
}

- (NSNumber *)fontSize {
	return _fontSize ? _fontSize : nil;
}

@end
