#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"

const NSInteger BLUR_STATUS_TAG = 78264801;
const NSInteger BLUR_TOPBAR_TAG = 78264802;
const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@implementation RNNNavigationOptions



-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)navigationOptions {
	self = [super init];
	self.statusBarHidden = [navigationOptions objectForKey:@"statusBarHidden"];
	self.screenBackgroundColor = [navigationOptions objectForKey:@"screenBackgroundColor"];
	self.backButtonTransition = [navigationOptions objectForKey:@"backButtonTransition"];
	self.orientation = [navigationOptions objectForKey:@"orientation"];
	self.leftButtons = [navigationOptions objectForKey:@"leftButtons"];
	self.rightButtons = [navigationOptions objectForKey:@"rightButtons"];
	self.topBar = [[RNNTopBarOptions alloc] initWithDict:[navigationOptions objectForKey:@"topBar"]];
	self.bottomTabs = [[RNNTabBarOptions alloc] initWithDict:[navigationOptions objectForKey:@"bottomTabs"]];
	self.sideMenu = [[RNNSideMenuOptions alloc] initWithDict:[navigationOptions objectForKey:@"sideMenu"]];
	self.backgroundImage = [RCTConvert UIImage:[navigationOptions objectForKey:@"backgroundImage"]];
	self.rootBackgroundImage = [RCTConvert UIImage:[navigationOptions objectForKey:@"rootBackgroundImage"]];
	self.tabItem = [[RNNTabItemOptions alloc] initWithDict:[navigationOptions objectForKey:@"bottomTab"]];
    
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([key isEqualToString:@"topBar"]) {
			[self.topBar mergeWith:[otherOptions objectForKey:key]];
		} else if ([key isEqualToString:@"bottomTabs"]) {
			[self.bottomTabs mergeWith:[otherOptions objectForKey:key]];
		} else if ([key isEqualToString:@"sideMenu"]) {
			[self.sideMenu mergeWith:[otherOptions objectForKey:@"sideMenu"]];
		} else if ([key isEqualToString:@"bottomTab"]) {
			[self.tabItem mergeWith:[otherOptions objectForKey:key]];
		} else {
			[self setValue:[otherOptions objectForKey:key] forKey:key];
		}
	}
}

-(void)applyOn:(UIViewController*)viewController {
	if (self.topBar) {
		if (self.topBar.backgroundColor) {
			UIColor* backgroundColor = [RCTConvert UIColor:self.topBar.backgroundColor];
			viewController.navigationController.navigationBar.barTintColor = backgroundColor;
		} else {
			viewController.navigationController.navigationBar.barTintColor = nil;
		}
		
		if (self.topBar.title) {
			viewController.navigationItem.title = self.topBar.title;
		}
		
		if (@available(iOS 11.0, *)) {
			if (self.topBar.largeTitle){
				if ([self.topBar.largeTitle boolValue]) {
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
		
		void (^disableTopBarTransparent)() = ^void(){
			UIView *transparentView = [viewController.navigationController.navigationBar viewWithTag:TOP_BAR_TRANSPARENT_TAG];
			if (transparentView){
				[transparentView removeFromSuperview];
				[viewController.navigationController.navigationBar setBackgroundImage:self.originalTopBarImages[@"backgroundImage"] forBarMetrics:UIBarMetricsDefault];
				viewController.navigationController.navigationBar.shadowImage = self.originalTopBarImages[@"shadowImage"];
				self.originalTopBarImages = nil;
			}
		};
	
		if (self.topBar.transparent) {
			if ([self.topBar.transparent boolValue]) {
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

		if (self.topBar.translucent) {
			viewController.navigationController.navigationBar.translucent = [self.topBar.translucent boolValue];
		}

		if (self.topBar.drawUnder) {
			if ([self.topBar.drawUnder boolValue]) {
				viewController.edgesForExtendedLayout |= UIRectEdgeTop;
			} else {
				viewController.edgesForExtendedLayout &= ~UIRectEdgeTop;
			}
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
		
		if (self.topBar.testID) {
			viewController.navigationController.navigationBar.accessibilityIdentifier = self.topBar.testID;
		}
	}
	
	if (self.popGesture) {
		viewController.navigationController.interactivePopGestureRecognizer.enabled = [self.popGesture boolValue];
	}
	
	if (self.screenBackgroundColor) {
		UIColor* screenColor = [RCTConvert UIColor:self.screenBackgroundColor];
		viewController.view.backgroundColor = screenColor;
	}
	
	if (self.bottomTabs) {
		if (self.bottomTabs.currentTabIndex) {
			[viewController.tabBarController setSelectedIndex:[self.bottomTabs.currentTabIndex unsignedIntegerValue]];
		}
		if (self.bottomTabs.hidden) {
			[((RNNTabBarController *)viewController.tabBarController) setTabBarHidden:[self.bottomTabs.hidden boolValue] animated:[self.bottomTabs.animateHide boolValue]];
		}
		
		if (self.bottomTabs.testID) {
			viewController.tabBarController.tabBar.accessibilityIdentifier = self.bottomTabs.testID;
		}
		
		if (self.bottomTabs.drawUnder) {
			if ([self.bottomTabs.drawUnder boolValue]) {
				viewController.edgesForExtendedLayout |= UIRectEdgeBottom;
			} else {
				viewController.edgesForExtendedLayout &= ~UIRectEdgeBottom;
			}
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
	
	RNNSideMenuController* sideMenuController = (RNNSideMenuController*)UIApplication.sharedApplication.delegate.window.rootViewController;
	if ([sideMenuController isKindOfClass:[RNNSideMenuController class]]) {
		if (self.sideMenu.leftSideVisible) {
			if (self.sideMenu.leftSideVisible.boolValue) {
				[sideMenuController showSideMenu:MMDrawerSideLeft animated:YES];
			} else {
				[sideMenuController hideSideMenu:MMDrawerSideLeft animated:YES];
			}
		}
		
		if (self.sideMenu.rightSideVisible) {
			if (self.sideMenu.rightSideVisible.boolValue) {
				[sideMenuController showSideMenu:MMDrawerSideRight animated:YES];
			} else {
				[sideMenuController hideSideMenu:MMDrawerSideRight animated:YES];
			}
		}
		
		[self.sideMenu resetOptions];
	}
	
	if (self.backgroundImage) {
		UIImageView* backgroundImageView = (viewController.view.subviews.count > 0) ? viewController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:viewController.view.bounds];
			[viewController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = self.backgroundImage;
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
	
	if (self.rootBackgroundImage) {
		UIImageView* backgroundImageView = (viewController.navigationController.view.subviews.count > 0) ? viewController.navigationController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:viewController.view.bounds];
			[viewController.navigationController.view insertSubview:backgroundImageView atIndex:0];
		}
		
		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = self.rootBackgroundImage;
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
	
	[self applyTabBarItemOptions:viewController];
}

- (void)applyTabBarItemOptions:(UIViewController*)viewController {
	if (self.tabItem) {
		if (self.tabItem.title || self.tabItem.icon) {
			UITabBarItem* tabItem = [[UITabBarItem alloc] initWithTitle:self.tabItem.title image:[RCTConvert UIImage:self.tabItem.icon] tag:self.tabItem.tag];
			tabItem.accessibilityIdentifier = self.tabItem.testID;
			[viewController.navigationController setTabBarItem:tabItem];
		}
		
		if (self.tabItem.badge) {
			NSString *badge = [RCTConvert NSString:self.tabItem.badge];
			if (viewController.navigationController) {
				viewController.navigationController.tabBarItem.badgeValue = badge;
			} else {
				viewController.tabBarItem.badgeValue = badge;
			}
		}
		
		if (self.tabItem.visible) {
			[viewController.tabBarController setSelectedIndex:[viewController.tabBarController.viewControllers indexOfObject:viewController]];
		}
		
		[self.tabItem resetOptions];
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
