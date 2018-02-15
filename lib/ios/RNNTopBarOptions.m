#import "RNNTopBarOptions.h"
#import "RNNNavigationButtons.h"
#import "RNNCustomTitleView.h"

extern const NSInteger BLUR_TOPBAR_TAG;

@interface RNNTopBarOptions ()

@property (nonatomic, strong) NSMutableDictionary* originalTopBarImages;
@property (nonatomic, strong) RNNNavigationButtons* navigationButtons;

@end

@implementation RNNTopBarOptions

- (void)applyOn:(UIViewController*)viewController {
	if (self.backgroundColor) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.backgroundColor];
		viewController.navigationController.navigationBar.barTintColor = backgroundColor;
	} else {
		viewController.navigationController.navigationBar.barTintColor = nil;
	}
	
	if (self.title) {
		viewController.navigationItem.title = self.title;
	}
	
	if (@available(iOS 11.0, *)) {
		if (self.largeTitle){
			if ([self.largeTitle boolValue]) {
				viewController.navigationController.navigationBar.prefersLargeTitles = YES;
				viewController.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
			} else {
				viewController.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeNever;
			}
		} else {
			viewController.navigationController.navigationBar.prefersLargeTitles = NO;
			viewController.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeNever;
		}
	}
	
	
	if (self.textFontFamily || self.textFontSize || self.textColor) {
		NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
		if (self.textColor) {
			navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:[self valueForKey:@"textColor"]];
		}
		if (self.textFontFamily){
			if(self.textFontSize) {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.textFontFamily size:[self.textFontSize floatValue]];
			} else {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.textFontFamily size:20];
			}
		} else if (self.textFontSize) {
			navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.textFontSize floatValue]];
		}
		viewController.navigationController.navigationBar.titleTextAttributes = navigationBarTitleTextAttributes;
		if (@available(iOS 11.0, *)){
			viewController.navigationController.navigationBar.largeTitleTextAttributes = navigationBarTitleTextAttributes;
		}
		
	}
	
	
	if (self.visible) {
		[viewController.navigationController setNavigationBarHidden:![self.visible boolValue] animated:[self.animateHide boolValue]];
	}
	
	if (self.hideOnScroll) {
		viewController.navigationController.hidesBarsOnSwipe = [self.hideOnScroll boolValue];
	}
	
	if (self.buttonColor) {
		UIColor* buttonColor = [RCTConvert UIColor:self.buttonColor];
		viewController.navigationController.navigationBar.tintColor = buttonColor;
	} else {
		viewController.navigationController.navigationBar.tintColor = nil;
	}
	
	if ([self.blur boolValue]) {
		if (![viewController.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG]) {
			
			[viewController.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
			viewController.navigationController.navigationBar.shadowImage = [UIImage new];
			UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
			CGRect statusBarFrame = [[UIApplication sharedApplication] statusBarFrame];
			blur.frame = CGRectMake(0, -1 * statusBarFrame.size.height, viewController.navigationController.navigationBar.frame.size.width, viewController.navigationController.navigationBar.frame.size.height + statusBarFrame.size.height);
			blur.userInteractionEnabled = NO;
			blur.tag = BLUR_TOPBAR_TAG;
			[viewController.navigationController.navigationBar insertSubview:blur atIndex:0];
			[viewController.navigationController.navigationBar sendSubviewToBack:blur];
		}
	} else {
		UIView *blur = [viewController.navigationController.navigationBar viewWithTag:BLUR_TOPBAR_TAG];
		if (blur) {
			[viewController.navigationController.navigationBar setBackgroundImage: nil forBarMetrics:UIBarMetricsDefault];
			viewController.navigationController.navigationBar.shadowImage = nil;
			[blur removeFromSuperview];
		}
	}
	
	void (^disableTopBarTransparent)(void) = ^ {
		UIView *transparentView = [viewController.navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
		if (transparentView){
			[transparentView removeFromSuperview];
			[viewController.navigationController.navigationBar setBackgroundImage:self.originalTopBarImages[@"backgroundImage"] forBarMetrics:UIBarMetricsDefault];
			viewController.navigationController.navigationBar.shadowImage = self.originalTopBarImages[@"shadowImage"];
			self.originalTopBarImages = nil;
		}
	};
	
	if (self.transparent) {
		if ([self.transparent boolValue]) {
			if (![viewController.navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG]){
				[self storeOriginalTopBarImages:viewController];
				[viewController.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
				viewController.navigationController.navigationBar.shadowImage = [UIImage new];
				UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
				transparentView.tag = TOP_BAR_TRANSPARENT_TAG;
				[viewController.navigationController.navigationBar insertSubview:transparentView atIndex:0];
			}
		} else {
			disableTopBarTransparent();
		}
	} else {
		disableTopBarTransparent();
	}
	
	if (self.translucent) {
		viewController.navigationController.navigationBar.translucent = [self.translucent boolValue];
	}
	
	if (self.drawBehind) {
		if ([self.drawBehind boolValue]) {
			viewController.edgesForExtendedLayout |= UIRectEdgeTop;
		} else {
			viewController.edgesForExtendedLayout &= ~UIRectEdgeTop;
		}
	}
	
	if (self.noBorder) {
		if ([self.noBorder boolValue]) {
			viewController.navigationController.navigationBar
			.shadowImage = [[UIImage alloc] init];
		} else {
			viewController.navigationController.navigationBar
			.shadowImage = nil;
		}
	}
	
	if (self.testID) {
		viewController.navigationController.navigationBar.accessibilityIdentifier = self.testID;
	}
	
	if (self.rightButtons || self.leftButtons) {
		_navigationButtons = [[RNNNavigationButtons alloc] initWithViewController:(RNNRootViewController*)viewController];
		[_navigationButtons applyLeftButtons:self.leftButtons rightButtons:self.rightButtons];
	}
}

-(void)storeOriginalTopBarImages:(UIViewController*)viewController {
	NSMutableDictionary *originalTopBarImages = [@{} mutableCopy];
	UIImage *bgImage = [viewController.navigationController.navigationBar backgroundImageForBarMetrics:UIBarMetricsDefault];
	if (bgImage != nil) {
		originalTopBarImages[@"backgroundImage"] = bgImage;
	}
	UIImage *shadowImage = viewController.navigationController.navigationBar.shadowImage;
	if (shadowImage != nil) {
		originalTopBarImages[@"shadowImage"] = shadowImage;
	}
	self.originalTopBarImages = originalTopBarImages;
}

@end


