#import "UINavigationController+RNNOptions.h"
#import "RNNFontAttributesCreator.h"
#import "UIImage+tint.h"
#import "UINavigationBar+utils.h"

const NSInteger BLUR_TOPBAR_TAG = 78264802;

@implementation UINavigationController (RNNOptions)

- (void)setInteractivePopGestureEnabled:(BOOL)enabled {
	self.interactivePopGestureRecognizer.enabled = enabled;
}

- (void)setRootBackgroundImage:(UIImage *)backgroundImage {
	UIImageView* backgroundImageView = (self.view.subviews.count > 0) ? self.view.subviews[0] : nil;
	if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
		backgroundImageView = [[UIImageView alloc] initWithFrame:self.view.bounds];
		[self.view insertSubview:backgroundImageView atIndex:0];
	}
	
	backgroundImageView.layer.masksToBounds = YES;
	backgroundImageView.image = backgroundImage;
	[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
}

- (void)setNavigationBarTestId:(NSString *)testID {
	self.navigationBar.accessibilityIdentifier = testID;
}

- (void)setNavigationBarVisible:(BOOL)visible animated:(BOOL)animated {
	[self setNavigationBarHidden:!visible animated:animated];
}

- (void)hideBarsOnScroll:(BOOL)hideOnScroll {
	self.hidesBarsOnSwipe = hideOnScroll;
}

- (void)setNavigationBarNoBorder:(BOOL)noBorder {
	[self.navigationBar rnn_showBorder:!noBorder];
}

- (void)setBarStyle:(UIBarStyle)barStyle {
	self.navigationBar.barStyle = barStyle;
}

- (void)setNavigationBarFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color {
    [self.navigationBar rnn_setNavigationBarTitleFontFamily:fontFamily fontSize:fontSize fontWeight:fontWeight color:color];
}

- (void)setNavigationBarLargeTitleVisible:(BOOL)visible {
	if (@available(iOS 11.0, *)) {
		if (visible){
			self.navigationBar.prefersLargeTitles = YES;
		} else {
			self.navigationBar.prefersLargeTitles = NO;
		}
	}
}

- (void)setNavigationBarLargeTitleFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color {
    [self.navigationBar rnn_setNavigationBarLargeTitleFontFamily:fontFamily fontSize:fontSize fontWeight:fontWeight color:color];
}

- (void)setNavigationBarTranslucent:(BOOL)translucent {
	self.navigationBar.translucent = translucent;
}

- (void)setNavigationBarBlur:(BOOL)blur {
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

- (void)setBackButtonColor:(UIColor *)color {
	self.navigationBar.tintColor = color;
}

- (void)setNavigationBarClipsToBounds:(BOOL)clipsToBounds {
	self.navigationBar.clipsToBounds = clipsToBounds;
}

- (void)setBackButtonIcon:(UIImage *)icon withColor:(UIColor *)color title:(NSString *)title showTitle:(BOOL)showTitle {
    UIBarButtonItem *backItem = [[UIBarButtonItem alloc] init];
    if (icon) {
        icon = color
        ? [[icon withTintColor:color] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal]
        : icon;
    }
    
    [self.navigationBar rnn_setBackIndicatorImage:icon];

    UIViewController *lastViewControllerInStack = self.viewControllers.count > 1 ? self.viewControllers[self.viewControllers.count - 2] : self.topViewController;

    if (showTitle) {
        backItem.title = title ? title : lastViewControllerInStack.navigationItem.title;
    }
    backItem.tintColor = color;

    lastViewControllerInStack.navigationItem.backBarButtonItem = backItem;
}

- (CGFloat)getTopBarHeight {
    return self.navigationBar.frame.size.height;
}

@end
