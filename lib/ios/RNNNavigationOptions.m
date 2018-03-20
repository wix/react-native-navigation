#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"
#import "RNNNavigationButtons.h"

const NSInteger BLUR_STATUS_TAG = 78264801;
const NSInteger BLUR_TOPBAR_TAG = 78264802;
const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@implementation RNNNavigationOptions

-(instancetype)init {
	return [self initWithDict:@{}];
}

-(instancetype)initWithDict:(NSDictionary *)options {
	self = [super init];
	self.statusBarHidden = [options objectForKey:@"statusBarHidden"];
	self.screenBackgroundColor = [options objectForKey:@"screenBackgroundColor"];
	self.backButtonTransition = [options objectForKey:@"backButtonTransition"];
	self.orientation = [options objectForKey:@"orientation"];
	self.topBar = [[RNNTopBarOptions alloc] initWithDict:[options objectForKey:@"topBar"]];
	self.topTab = [[RNNTopTabOptions alloc] initWithDict:[options objectForKey:@"topTab"]];
	self.bottomTabs = [[RNNBottomTabsOptions alloc] initWithDict:[options objectForKey:@"bottomTabs"]];
	self.sideMenu = [[RNNSideMenuOptions alloc] initWithDict:[options objectForKey:@"sideMenu"]];
	self.backgroundImage = [RCTConvert UIImage:[options objectForKey:@"backgroundImage"]];
	self.rootBackgroundImage = [RCTConvert UIImage:[options objectForKey:@"rootBackgroundImage"]];
	self.bottomTab = [[RNNBottomTabOptions alloc] initWithDict:[options objectForKey:@"bottomTab"]];
	self.overlay = [[RNNOverlayOptions alloc] initWithDict:[options objectForKey:@"overlay"]];
	self.animated = [options objectForKey:@"animated"];
	self.customTransition = [[RNNAnimationOptions alloc] initWithDict:[options objectForKey:@"customTransition"]];
	self.animations = [[RNNTransitionsOptions alloc] initWithDict:[options objectForKey:@"animations"]];
	
	return self;
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	for (id key in otherOptions) {
		if ([self hasProperty:key]) {
			if ([[self valueForKey:key] isKindOfClass:[RNNOptions class]]) {
				RNNOptions* options = [self valueForKey:key];
				[options mergeWith:[otherOptions objectForKey:key]];
			} else {
				[self setValue:[otherOptions objectForKey:key] forKey:key];
			}
		}
	}
}

-(void)applyOn:(UIViewController*)viewController {
//	[_defaultOptions applyOn:viewController];
	
	[self.topBar applyOn:viewController];
	[self.bottomTabs applyOn:viewController];
	[self.topTab applyOn:viewController];
	[self.bottomTab applyOn:viewController];
	[self.sideMenu applyOn:viewController];
	[self.overlay applyOn:viewController];
	[self applyOtherOptionsOn:viewController];
}

- (void)applyOtherOptionsOn:(UIViewController*)viewController {
	if (self.popGesture) {
		viewController.navigationController.interactivePopGestureRecognizer.enabled = [self.popGesture boolValue];
	}
	
	if (self.screenBackgroundColor) {
		UIColor* screenColor = [RCTConvert UIColor:self.screenBackgroundColor];
		viewController.view.backgroundColor = screenColor;
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
