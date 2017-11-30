#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"

const NSInteger BLUR_STATUS_TAG = 78264801;
const NSInteger BLUR_TOPBAR_TAG = 78264802;

@implementation RNNNavigationOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)navigationOptions {
	self = [super init];
	self.statusBarHidden = [navigationOptions objectForKey:@"statusBarHidden"];
	self.screenBackgroundColor = [navigationOptions objectForKey:@"screenBackgroundColor"];
	self.orientation = [navigationOptions objectForKey:@"orientation"];
	self.leftButtons = [navigationOptions objectForKey:@"leftButtons"];
	self.rightButtons = [navigationOptions objectForKey:@"rightButtons"];
	self.topBar = [[RNNTopBarOptions alloc] initWithDict:[navigationOptions objectForKey:@"topBar"]];
	self.tabBar = [[RNNTabBarOptions alloc] initWithDict:[navigationOptions objectForKey:@"tabBar"]];
	
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([key isEqualToString:@"topBar"]) {
			[self.topBar mergeWith:[otherOptions objectForKey:key]];
		} else if ([key isEqualToString:@"tabBar"]) {
			[self.tabBar mergeWith:[otherOptions objectForKey:key]];
		} else {
			[self setValue:[otherOptions objectForKey:key] forKey:key];
		}
	}
}

-(void)applyOn:(UIViewController*)viewController {
	if (self.topBar) {
		if(self.topBar.backgroundColor) {
			UIColor* backgroundColor = [RCTConvert UIColor:self.topBar.backgroundColor];
			viewController.navigationController.navigationBar.barTintColor = backgroundColor;
		} else {
			viewController.navigationController.navigationBar.barTintColor = nil;
		}
		
		if (self.topBar.title) {
			viewController.navigationItem.title = self.topBar.title;
		}
		
		if (self.topBar.textFontFamily || self.topBar.textFontSize || self.topBar.textColor) {
			NSMutableDictionary* navigationBarTitleTextAttributes = [NSMutableDictionary new];
			if (self.topBar.textColor) {
				navigationBarTitleTextAttributes[NSForegroundColorAttributeName] = [RCTConvert UIColor:[self.topBar valueForKey:@"textColor"]];
			}
			if (self.topBar.textFontFamily){
				if(self.topBar.textFontSize) {
					navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.topBar.textFontFamily size:[self.topBar.textFontSize floatValue]];
				} else {
					navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont fontWithName:self.topBar.textFontFamily size:20];
				}
			} else if (self.topBar.textFontSize) {
				navigationBarTitleTextAttributes[NSFontAttributeName] = [UIFont systemFontOfSize:[self.topBar.textFontSize floatValue]];
			}
			viewController.navigationController.navigationBar.titleTextAttributes = navigationBarTitleTextAttributes;
		}
		
		
		if (self.topBar.hidden){
			[viewController.navigationController setNavigationBarHidden:[self.topBar.hidden boolValue] animated:[self.topBar.animateHide boolValue]];
		}
		
		if (self.topBar.hideOnScroll) {
			viewController.navigationController.hidesBarsOnSwipe = [self.topBar.hideOnScroll boolValue];
		}
		
		if (self.topBar.buttonColor) {
			UIColor* buttonColor = [RCTConvert UIColor:self.topBar.buttonColor];
			viewController.navigationController.navigationBar.tintColor = buttonColor;
		} else {
			viewController.navigationController.navigationBar.tintColor = nil;
		}
		
		if ([self.topBar.blur boolValue]) {
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
		
		if (self.topBar.translucent) {
			viewController.navigationController.navigationBar.translucent = [self.topBar.translucent boolValue];
		}
		
		if (self.topBar.transparent) {
			[viewController.navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
			viewController.navigationController.navigationBar.shadowImage = [UIImage new];
			UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
			[viewController.navigationController.navigationBar insertSubview:transparentView atIndex:0];
		}
		
		if (self.topBar.noBorder) {
			if ([self.topBar.noBorder boolValue]) {
				viewController.navigationController.navigationBar
				.shadowImage = [[UIImage alloc] init];
			} else {
				viewController.navigationController.navigationBar
				.shadowImage = nil;
			}
		}
	}
	
	if (self.screenBackgroundColor) {
		UIColor* screenColor = [RCTConvert UIColor:self.screenBackgroundColor];
		viewController.view.backgroundColor = screenColor;
	}
	
	if (self.tabBar) {
		if (self.tabBar.tabBadge) {
			NSString *badge = [RCTConvert NSString:self.tabBar.tabBadge];
			if (viewController.navigationController) {
				viewController.navigationController.tabBarItem.badgeValue = badge;
			} else {
				viewController.tabBarItem.badgeValue = badge;
			}
		}
		if (self.tabBar.currentTabIndex) {
			[viewController.tabBarController setSelectedIndex:[self.tabBar.currentTabIndex unsignedIntegerValue]];
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
