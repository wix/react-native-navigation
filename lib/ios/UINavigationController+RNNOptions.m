#import "UINavigationController+RNNOptions.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+tint.h"

const NSInteger BLUR_TOPBAR_TAG = 78264802;

@implementation UINavigationController (RNNOptions)

#if !TARGET_OS_TV
- (void)rnn_setInteractivePopGestureEnabled:(BOOL)enabled {
	self.interactivePopGestureRecognizer.enabled = enabled;
}
#endif

- (void)rnn_setRootBackgroundImage:(UIImage *)backgroundImage {
	UIImageView* backgroundImageView = (self.view.subviews.count > 0) ? self.view.subviews[0] : nil;
	if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
		backgroundImageView = [[UIImageView alloc] initWithFrame:self.view.bounds];
		[self.view insertSubview:backgroundImageView atIndex:0];
	}
	
	backgroundImageView.layer.masksToBounds = YES;
	backgroundImageView.image = backgroundImage;
	[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
}

- (void)rnn_setNavigationBarTestID:(NSString *)testID {
	self.navigationBar.accessibilityIdentifier = testID;
}

- (void)rnn_setNavigationBarVisible:(BOOL)visible animated:(BOOL)animated {
	[self setNavigationBarHidden:!visible animated:animated];
}

#if !TARGET_OS_TV
- (void)rnn_hideBarsOnScroll:(BOOL)hideOnScroll {
	self.hidesBarsOnSwipe = hideOnScroll;
}
#endif

- (void)rnn_setNavigationBarNoBorder:(BOOL)noBorder {
	if (noBorder) {
		[self.navigationBar setShadowImage:[[UIImage alloc] init]];
	} else {
		[self.navigationBar setShadowImage:nil];
	}
}

#if !TARGET_OS_TV
- (void)rnn_setBarStyle:(UIBarStyle)barStyle {
	self.navigationBar.barStyle = barStyle;
}
#endif

- (void)rnn_setNavigationBarFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize color:(UIColor *)color {
	NSDictionary* fontAttributes = [RNNFontAttributesCreator createFontAttributesWithFontFamily:fontFamily fontSize:fontSize color:color];
	
	if (fontAttributes.allKeys.count > 0) {
		self.navigationBar.titleTextAttributes = fontAttributes;
	}
}

#if !TARGET_OS_TV
- (void)rnn_setNavigationBarLargeTitleVisible:(BOOL)visible {
	if (@available(iOS 11.0, *)) {
		if (visible){
			self.navigationBar.prefersLargeTitles = YES;
		} else {
			self.navigationBar.prefersLargeTitles = NO;
		}
	}
}
#endif

#if !TARGET_OS_TV
- (void)rnn_setNavigationBarLargeTitleFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize color:(UIColor *)color {
	if (@available(iOS 11.0, *)) {
		NSDictionary* fontAttributes = [RNNFontAttributesCreator createFontAttributesWithFontFamily:fontFamily fontSize:fontSize color:color];
		self.navigationBar.largeTitleTextAttributes = fontAttributes;
	}
}
#endif

- (void)rnn_setNavigationBarTranslucent:(BOOL)translucent {
	self.navigationBar.translucent = translucent;
}

#if !TARGET_OS_TV
- (void)rnn_setNavigationBarBlur:(BOOL)blur {
	if (blur && ![self.navigationBar viewWithTag:BLUR_TOPBAR_TAG]) {
		[self.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
		self.navigationBar.shadowImage = [UIImage new];
		UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
		CGRect statusBarFrame = [[UIApplication sharedApplication] statusBarFrame];
		blur.frame = CGRectMake(0, -1 * statusBarFrame.size.height, self.navigationBar.frame.size.width, self.navigationBar.frame.size.height + statusBarFrame.size.height);
		blur.userInteractionEnabled = NO;
		blur.tag = BLUR_TOPBAR_TAG;
		[self.navigationBar insertSubview:blur atIndex:0];
		[self.navigationBar sendSubviewToBack:blur];
	} else {
		UIView *blur = [self.navigationBar viewWithTag:BLUR_TOPBAR_TAG];
		if (blur) {
			[self.navigationBar setBackgroundImage: nil forBarMetrics:UIBarMetricsDefault];
			self.navigationBar.shadowImage = nil;
			[blur removeFromSuperview];
		}
	}
}
#endif

#if !TARGET_OS_TV
- (void)rnn_setBackButtonIcon:(UIImage *)icon withColor:(UIColor *)color title:(NSString *)title {
	UIBarButtonItem *backItem = [[UIBarButtonItem alloc] init];
	if (icon) {
		backItem.image = color
		? [[icon withTintColor:color] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
		: icon;
		
		[self.navigationBar setBackIndicatorImage:[UIImage new]];
		[self.navigationBar setBackIndicatorTransitionMaskImage:[UIImage new]];
	}
	
	UIViewController *lastViewControllerInStack = self.viewControllers.count > 1 ? [self.viewControllers objectAtIndex:self.viewControllers.count-2] : self.topViewController;
	
	backItem.title = title ? title : lastViewControllerInStack.navigationItem.title;
	backItem.tintColor = color;
	
	lastViewControllerInStack.navigationItem.backBarButtonItem = backItem;
}
#endif

- (void)rnn_setBackButtonColor:(UIColor *)color {
	self.navigationBar.tintColor = color;
}

- (void)rnn_setNavigationBarClipsToBounds:(BOOL)clipsToBounds {
	self.navigationBar.clipsToBounds = clipsToBounds;
}

@end
