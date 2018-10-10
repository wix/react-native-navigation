#import "RNNBackgroundOptions.h"
#import "UINavigationController+RNNOptions.h"

extern const NSInteger BLUR_TOPBAR_TAG;
const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@interface RNNBackgroundOptions()

@property (nonatomic, strong) NSMutableDictionary* originalTopBarImages;

@end

@implementation RNNBackgroundOptions

- (void)applyOnNavigationController:(UINavigationController *)navigationController {
	if (self.translucent) {
		[navigationController rnn_setNavigationBarTranslucent:[self.translucent boolValue]];
	}
	
	[navigationController rnn_setNavigationBarBlur:[self.blur boolValue]];
	
	
	if (self.color && ![self.color isKindOfClass:[NSNull class]]) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.color];

		CGFloat bgColorAlpha = CGColorGetAlpha(backgroundColor.CGColor);

		if (bgColorAlpha == 0.0) {
			if (![navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG]){
				[self storeOriginalTopBarImages:navigationController];
				UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
				transparentView.backgroundColor = [UIColor clearColor];
				transparentView.tag = TOP_BAR_TRANSPARENT_TAG;
				[navigationController.navigationBar insertSubview:transparentView atIndex:0];
			}
			navigationController.navigationBar.translucent = YES;
			[navigationController.navigationBar setBackgroundColor:[UIColor clearColor]];
			navigationController.navigationBar.shadowImage = [UIImage new];
			[navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
		} else {
			navigationController.navigationBar.barTintColor = backgroundColor;
			UIView *transparentView = [navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
			if (transparentView){
				[transparentView removeFromSuperview];
				[navigationController.navigationBar setBackgroundImage:self.originalTopBarImages[@"backgroundImage"] forBarMetrics:UIBarMetricsDefault];
				navigationController.navigationBar.shadowImage = self.originalTopBarImages[@"shadowImage"];
				self.originalTopBarImages = nil;
			}
		}
	} else {
		UIView *transparentView = [navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
		if (transparentView){
			[transparentView removeFromSuperview];
			[navigationController.navigationBar setBackgroundImage:self.originalTopBarImages[@"backgroundImage"] forBarMetrics:UIBarMetricsDefault];
			navigationController.navigationBar.shadowImage = self.originalTopBarImages[@"shadowImage"];
			self.originalTopBarImages = nil;
		}
	}

	if (self.clipToBounds) {
		[navigationController rnn_setNavigationBarClipsToBounds:[self.clipToBounds boolValue]];
	}
}

- (void)storeOriginalTopBarImages:(UINavigationController *)navigationController {
	NSMutableDictionary *originalTopBarImages = [@{} mutableCopy];
	UIImage *bgImage = [navigationController.navigationBar backgroundImageForBarMetrics:UIBarMetricsDefault];
	if (bgImage != nil) {
		originalTopBarImages[@"backgroundImage"] = bgImage;
	}
	UIImage *shadowImage = navigationController.navigationBar.shadowImage;
	if (shadowImage != nil) {
		originalTopBarImages[@"shadowImage"] = shadowImage;
	}
	self.originalTopBarImages = originalTopBarImages;
}

@end
