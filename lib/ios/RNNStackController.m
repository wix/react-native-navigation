#import "RNNStackController.h"
#import "RNNComponentViewController.h"
#import "UINavigationBar+utils.h"

@implementation RNNStackController

-(void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	[super setDefaultOptions:defaultOptions];
	[self.presenter setDefaultOptions:defaultOptions];
}

- (void)viewDidLayoutSubviews {
	[super viewDidLayoutSubviews];
	[self.presenter applyOptionsOnViewDidLayoutSubviews:self.resolveOptions];
}

- (UIViewController *)getCurrentChild {
	return self.topViewController;
}

- (CGFloat)getTopBarHeight {
	return self.navigationBar.frame.size.height;
}

- (UINavigationController *)navigationController {
	return self;
}

- (UIStatusBarStyle)preferredStatusBarStyle {
	return [_presenter getStatusBarStyle:self.resolveOptions];
}

- (UIModalPresentationStyle)modalPresentationStyle {
	return self.getCurrentChild.modalPresentationStyle;
}

- (UIViewController *)popViewControllerAnimated:(BOOL)animated {
	if (self.viewControllers.count > 1) {
		UIViewController *controller = self.viewControllers[self.viewControllers.count - 2];
		if ([controller isKindOfClass:[RNNComponentViewController class]]) {
			RNNComponentViewController *rnnController = (RNNComponentViewController *)controller;
			[self.presenter applyOptionsBeforePopping:rnnController.resolveOptions];
		}
	}
	
	return [super popViewControllerAnimated:animated];
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

- (void)setTopBarBackgroundColor:(UIColor *)backgroundColor {
	[self.navigationBar rnn_setBackgroundColor:backgroundColor];
}

@end
