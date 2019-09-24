#import "UINavigationController+RNNOptions.h"
#import "RNNFontAttributesCreator.h"

const NSInteger BLUR_TOPBAR_TAG = 78264802;

@implementation UINavigationController (RNNOptions)

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
	if (noBorder) {
		[self.navigationBar setShadowImage:[[UIImage alloc] init]];
		if (@available(iOS 13.0, *)) {
			[self getNavigaitonBarStandardAppearance].shadowImage =  [[UIImage alloc] init];
			[self getNavigaitonBarCompactAppearance].shadowImage = [[UIImage alloc] init];
			[self getNavigaitonBarScrollEdgeAppearance].shadowImage = [[UIImage alloc] init];
		}
	} else {
		[self.navigationBar setShadowImage:nil];
		if (@available(iOS 13.0, *)) {
			[self getNavigaitonBarStandardAppearance].shadowImage =  nil;
			[self getNavigaitonBarCompactAppearance].shadowImage = nil;
			[self getNavigaitonBarScrollEdgeAppearance].shadowImage = nil;
		}
	}
}

- (void)setBarStyle:(UIBarStyle)barStyle {
	self.navigationBar.barStyle = barStyle;
}

- (void)setNavigationBarFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color {
	NSDictionary* fontAttributes = [RNNFontAttributesCreator createWithFontFamily:fontFamily fontSize:fontSize fontWeight:fontWeight color:color];
	
	if (fontAttributes.allKeys.count > 0) {
		self.navigationBar.titleTextAttributes = fontAttributes;
		if (@available(iOS 13.0, *)) {
			[self getNavigaitonBarStandardAppearance].titleTextAttributes =  fontAttributes;
			[self getNavigaitonBarCompactAppearance].titleTextAttributes = fontAttributes;
			[self getNavigaitonBarScrollEdgeAppearance].titleTextAttributes = fontAttributes;
		}
	}
}

- (void)setNavigationBarLargeTitleVisible:(BOOL)visible API_AVAILABLE(ios(11.0)) {
	if (@available(iOS 11.0, *)) {
		if (visible){
			self.navigationBar.prefersLargeTitles = YES;
		} else {
			self.navigationBar.prefersLargeTitles = NO;
		}
	}
}

- (void)setNavigationBarLargeTitleFontFamily:(NSString *)fontFamily fontSize:(NSNumber *)fontSize fontWeight:(NSString *)fontWeight color:(UIColor *)color API_AVAILABLE(ios(11.0)) {
	if (@available(iOS 11.0, *)) {
		NSDictionary* fontAttributes = [RNNFontAttributesCreator createWithFontFamily:fontFamily fontSize:fontSize fontWeight:fontWeight color:color];
		self.navigationBar.largeTitleTextAttributes = fontAttributes;
		if (@available(iOS 13.0, *)) {
			[self getNavigaitonBarStandardAppearance].largeTitleTextAttributes =  fontAttributes;
			[self getNavigaitonBarCompactAppearance].largeTitleTextAttributes = fontAttributes;
			[self getNavigaitonBarScrollEdgeAppearance].largeTitleTextAttributes = fontAttributes;
		}
	}
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

@end
