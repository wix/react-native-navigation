#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"

const NSInteger BLUR_STATUS_TAG = 78264801;
const NSInteger BLUR_TOPBAR_TAG = 78264802;

@implementation RNNNavigationOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)navigationOptions {
	self = [super init];
	self.topBarBackgroundColor = [navigationOptions objectForKey:@"topBarBackgroundColor"];
	self.statusBarHidden = [navigationOptions objectForKey:@"statusBarHidden"];
	self.title = [navigationOptions objectForKey:@"title"];
	self.topBarTextColor = [navigationOptions objectForKey:@"topBarTextColor"];
	self.screenBackgroundColor = [navigationOptions objectForKey:@"screenBackgroundColor"];
	self.topBarTextFontFamily = [navigationOptions objectForKey:@"topBarTextFontFamily"];
	self.topBarHidden = [navigationOptions objectForKey:@"topBarHidden"];
	self.topBarHideOnScroll = [navigationOptions objectForKey:@"topBarHideOnScroll"];
	self.topBarButtonColor = [navigationOptions objectForKey:@"topBarButtonColor"];
	self.topBarTranslucent = [navigationOptions objectForKey:@"topBarTranslucent"];
	self.tabBadge = [navigationOptions objectForKey:@"tabBadge"];
	self.topBarTextFontSize = [navigationOptions objectForKey:@"topBarTextFontSize"];
	self.orientation = [navigationOptions objectForKey:@"orientation"];
	self.leftButtons = [navigationOptions objectForKey:@"leftButtons"];
	self.rightButtons = [navigationOptions objectForKey:@"rightButtons"];
	self.topBarNoBorder = [navigationOptions objectForKey:@"topBarNoBorder"];
	self.topBarBlur = [navigationOptions objectForKey:@"topBarBlur"];
	self.tabBarHidden = [navigationOptions objectForKey:@"tabBarHidden"];
	self.tabBarTranslucent = [navigationOptions objectForKey:@"tabBarTranslucent"];
	self.tabBarHideShadow = [navigationOptions objectForKey:@"tabBarHideShadow"];
	self.tabBarBackgroundColor = [navigationOptions objectForKey:@"tabBarBackgroundColor"];
	self.tabBarTextColor = [navigationOptions objectForKey:@"tabBarTextColor"];
	self.tabBarSelectedTextColor = [navigationOptions objectForKey:@"tabBarSelectedTextColor"];
	self.tabBarTextFontFamily = [navigationOptions objectForKey:@"tabBarTextFontFamily"];
	self.tabBarTextFontSize = [navigationOptions objectForKey:@"tabBarTextFontSize"];

	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		[self setValue:[otherOptions objectForKey:key] forKey:key];
	}
}

-(void)applyOn:(UIViewController*)viewController {
	if (self.topBarBackgroundColor) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.topBarBackgroundColor];
		viewController.navigationController.navigationBar.barTintColor = backgroundColor;
	} else {
		viewController.navigationController.navigationBar.barTintColor = nil;
	}

	if (self.title) {
		viewController.navigationItem.title = self.title;
	}

	if (self.topBarTextFont || self.topBarTextColor){
		NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
		if (self.topBarTextColor) {
			navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.topBarTextColor];
		}
		if (self.topBarTextFont){
			navigationBarTitleTextAttributes[NSFontAttributeName] = self.topBarTextFont;
		}
		viewController.navigationController.navigationBar.titleTextAttributes = navigationBarTitleTextAttributes;
	}

	if (self.tabBarBackgroundColor) {
		viewController.tabBarController.tabBar.barTintColor = [RCTConvert UIColor:self.tabBarBackgroundColor];
	}

	if (self.tabBarTranslucent) {
		viewController.tabBarController.tabBar.translucent = [self.tabBarTranslucent boolValue];
	}

	if (self.tabBarHideShadow) {
		viewController.tabBarController.tabBar.clipsToBounds = [self.tabBarHideShadow boolValue];
	}

	if (self.tabBarTextFont || self.tabBarTextColor){
		NSMutableDictionary* tabBarTitleTextAttributes = [NSMutableDictionary new];
		if (self.tabBarTextColor) {
			tabBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.tabBarTextColor];
		}
		if (self.tabBarTextFont){
			tabBarTitleTextAttributes[NSFontAttributeName] = self.tabBarTextFont;
		}
		for(UITabBarItem* item in viewController.tabBarController.tabBar.items) {
			[item setTitleTextAttributes:tabBarTitleTextAttributes forState:UIControlStateNormal];
		}
	}

	if (self.tabBarSelectedTextColor){
		for(UITabBarItem* item in viewController.tabBarController.tabBar.items) {
			NSMutableDictionary* tabBarTitleTextAttributes = [NSMutableDictionary new];
			tabBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:self.tabBarSelectedTextColor];
			[item setTitleTextAttributes:tabBarTitleTextAttributes forState:UIControlStateSelected];
		}
	}

	if (self.screenBackgroundColor) {
		UIColor* screenColor = [RCTConvert UIColor:self.screenBackgroundColor];
		viewController.view.backgroundColor = screenColor;
	}

	if (self.topBarHidden){
		if ([self.topBarHidden boolValue]) {
			[viewController.navigationController setNavigationBarHidden:YES animated:YES];
		} else {
			[viewController.navigationController setNavigationBarHidden:NO animated:YES];
		}
	}

	if (self.topBarHideOnScroll) {
		BOOL topBarHideOnScrollBool = [self.topBarHideOnScroll boolValue];
		if (topBarHideOnScrollBool) {
			viewController.navigationController.hidesBarsOnSwipe = YES;
		} else {
			viewController.navigationController.hidesBarsOnSwipe = NO;
		}
	}

	if (self.topBarButtonColor) {
		UIColor* buttonColor = [RCTConvert UIColor:self.topBarButtonColor];
		viewController.navigationController.navigationBar.tintColor = buttonColor;
	} else {
		viewController.navigationController.navigationBar.tintColor = nil;
	}

	if (self.tabBadge) {
		NSString *badge = [RCTConvert NSString:self.tabBadge];
		if (viewController.navigationController) {
			viewController.navigationController.tabBarItem.badgeValue = badge;
		} else {
			viewController.tabBarItem.badgeValue = badge;
		}
	}

	if (self.topBarTranslucent) {
		if ([self.topBarTranslucent boolValue]) {
			viewController.navigationController.navigationBar.translucent = YES;
		} else {
			viewController.navigationController.navigationBar.translucent = NO;
		}
	}

	if (self.topBarNoBorder) {
		if ([self.topBarNoBorder boolValue]) {
			viewController.navigationController.navigationBar
			.shadowImage = [[UIImage alloc] init];
		} else {
			viewController.navigationController.navigationBar
			.shadowImage = nil;
		}
	}

	if (self.statusBarBlur) {
		UIView* curBlurView = [viewController.view viewWithTag:BLUR_STATUS_TAG];
		if ([self.statusBarBlur boolValue]) {
			if (!curBlurView) {
				UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
				blur.frame = [[UIApplication sharedApplication] statusBarFrame];
				blur.tag = BLUR_STATUS_TAG;
				[viewController.view insertSubview:blur atIndex:0];
			}
		} else {
			if (curBlurView) {
				[curBlurView removeFromSuperview];
			}
		}
	}

	if (self.topBarBlur && [self.topBarBlur boolValue]) {

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
}

-(UIFont *)topBarTextFont {
	if (self.topBarTextFontFamily) {
		return [UIFont fontWithName:self.topBarTextFontFamily size:self.topBarTextFontSizeValue];
	}
	else if (self.topBarTextFontSize) {
		return [UIFont systemFontOfSize:self.topBarTextFontSizeValue];
	}
	else {
		return nil;
	}
}

-(CGFloat)topBarTextFontSizeValue {
	return self.topBarTextFontSize ? [self.topBarTextFontSize floatValue] : 20;
}


-(UIFont *)tabBarTextFont {
	if (self.tabBarTextFontFamily) {
		return [UIFont fontWithName:self.tabBarTextFontFamily size:self.tabBarTextFontSizeValue];
	}
	else if (self.tabBarTextFontSize) {
		return [UIFont systemFontOfSize:self.tabBarTextFontSizeValue];
	}
	else {
		return nil;
	}
}

-(CGFloat)tabBarTextFontSizeValue {
	return self.tabBarTextFontSize ? [self.tabBarTextFontSize floatValue] : 10;
}

- (UIInterfaceOrientationMask)supportedOrientations {
	NSArray* orientationsArray = [self.orientation isKindOfClass:[NSString class]] ? @[self.orientation] : self.orientation;
	NSUInteger supportedOrientationsMask = 0;
	if (!orientationsArray || [self.orientation isEqual:@"default"]) {
		return [[UIApplication sharedApplication] supportedInterfaceOrientationsForWindow:[[UIApplication sharedApplication] keyWindow]];
	} else {
		for (NSString* orientation in orientationsArray) {
			if ([orientation isEqualToString:@"all"]) {
				supportedOrientationsMask = UIInterfaceOrientationMaskAll;
				break;
			}
			if ([orientation isEqualToString:@"landscape"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskLandscape);
			}
			if ([orientation isEqualToString:@"portrait"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskPortrait);
			}
			if ([orientation isEqualToString:@"upsideDown"]) {
				supportedOrientationsMask = (supportedOrientationsMask | UIInterfaceOrientationMaskPortraitUpsideDown);
			}
		}
	}

	return supportedOrientationsMask;
}


@end
