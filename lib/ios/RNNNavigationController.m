#import "RNNNavigationController.h"
#import "RNNRootViewController.h"

const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@implementation RNNNavigationController

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
		if ([controller isKindOfClass:[RNNRootViewController class]]) {
			RNNRootViewController *rnnController = (RNNRootViewController *)controller;
			[self.presenter applyOptionsBeforePopping:rnnController.resolveOptions];
		}
	}
	
	return [super popViewControllerAnimated:animated];
}

- (UIViewController *)childViewControllerForStatusBarStyle {
	return self.topViewController;
}

- (void)setTopBarBackgroundColor:(UIColor *)backgroundColor {
	if (backgroundColor) {
		CGFloat bgColorAlpha = CGColorGetAlpha(backgroundColor.CGColor);
		
		if (bgColorAlpha == 0.0) {
			if (![self.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG]){
				UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
				transparentView.backgroundColor = [UIColor clearColor];
				transparentView.tag = TOP_BAR_TRANSPARENT_TAG;
				[self.navigationBar insertSubview:transparentView atIndex:0];
			}
			self.navigationBar.translucent = YES;
			[self.navigationBar setBackgroundColor:[UIColor clearColor]];
			self.navigationBar.shadowImage = [UIImage new];
			[self.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
		} else {
			self.navigationBar.barTintColor = backgroundColor;
			UIView *transparentView = [self.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
			if (transparentView){
				[transparentView removeFromSuperview];
			}
		}
	} else {
		UIView *transparentView = [self.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
		if (transparentView){
			[transparentView removeFromSuperview];
		}
		
		self.navigationBar.barTintColor = nil;
	}
}

@end
