#import "RNNStackController.h"
#import "RNNComponentViewController.h"

const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@implementation RNNStackController

- (UINavigationBarAppearance*)getNavigaitonBarStandardAppearance  API_AVAILABLE(ios(13.0)) {
	if (!self.navigationBar.standardAppearance) {
		self.navigationBar.standardAppearance = [UINavigationBarAppearance new];
	}
	return self.navigationBar.standardAppearance;
}

- (UINavigationBarAppearance*)getNavigaitonBarCompactAppearance  API_AVAILABLE(ios(13.0)) {
	if (!self.navigationBar.compactAppearance) {
		self.navigationBar.compactAppearance = [UINavigationBarAppearance new];
	}
	return self.navigationBar.compactAppearance;
}

- (UINavigationBarAppearance*)getNavigaitonBarScrollEdgeAppearance  API_AVAILABLE(ios(13.0)) {
	if (!self.navigationBar.scrollEdgeAppearance) {
		self.navigationBar.scrollEdgeAppearance = [UINavigationBarAppearance new];
	}
	return self.navigationBar.scrollEdgeAppearance;
}


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
			if (@available(iOS 13.0, *)) {
				UINavigationBarAppearance *standardAppearance = [self getNavigaitonBarStandardAppearance];
				standardAppearance.backgroundColor = [UIColor clearColor];
				standardAppearance.shadowImage =  [UIImage new];
				standardAppearance.backgroundImage = [UIImage new];
				
				UINavigationBarAppearance *compactAppearance = [self getNavigaitonBarCompactAppearance];
				compactAppearance.backgroundColor = [UIColor clearColor];
				compactAppearance.shadowImage =  [UIImage new];
				compactAppearance.backgroundImage = [UIImage new];
				
				UINavigationBarAppearance *scrollEdgeAppearance = [self getNavigaitonBarScrollEdgeAppearance];
				scrollEdgeAppearance.backgroundColor = [UIColor clearColor];
				scrollEdgeAppearance.shadowImage =  [UIImage new];
				scrollEdgeAppearance.backgroundImage = [UIImage new];
			}
		} else {
			self.navigationBar.barTintColor = backgroundColor;
			if (@available(iOS 13.0, *)) {
				[self getNavigaitonBarStandardAppearance].backgroundColor =  backgroundColor;
				[self getNavigaitonBarCompactAppearance].backgroundColor = backgroundColor;
				[self getNavigaitonBarScrollEdgeAppearance].backgroundColor = backgroundColor;
			}
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
		if (@available(iOS 13.0, *)) {
			[self getNavigaitonBarStandardAppearance].backgroundColor =  nil;
			[self getNavigaitonBarCompactAppearance].backgroundColor = nil;
			[self getNavigaitonBarScrollEdgeAppearance].backgroundColor = nil;
		}
	}
}

@end
