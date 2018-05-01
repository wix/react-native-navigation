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

@implementation RCTConvert (UIModalPresentationStyle)

RCT_ENUM_CONVERTER(UIModalPresentationStyle,
				   (@{@"fullScreen": @(UIModalPresentationFullScreen),
					  @"pageSheet": @(UIModalPresentationPageSheet),
					  @"formSheet": @(UIModalPresentationFormSheet),
					  @"currentContext": @(UIModalPresentationCurrentContext),
					  @"custom": @(UIModalPresentationCustom),
					  @"overFullScreen": @(UIModalPresentationOverFullScreen),
					  @"overCurrentContext": @(UIModalPresentationOverCurrentContext),
					  @"popover": @(UIModalPresentationPopover),
					  @"none": @(UIModalPresentationNone)
					  }), UIModalPresentationFullScreen, integerValue)

@end

@implementation RCTConvert (UIModalTransitionStyle)

RCT_ENUM_CONVERTER(UIModalTransitionStyle,
                   (@{@"coverVertical": @(UIModalTransitionStyleCoverVertical),
                      @"flipHorizontal": @(UIModalTransitionStyleFlipHorizontal),
                      @"crossDissolve": @(UIModalTransitionStyleCrossDissolve),
                      @"partialCurl": @(UIModalTransitionStylePartialCurl)
                      }), UIModalTransitionStyleCoverVertical, integerValue)

@end

@implementation RNNNavigationOptions


-(void)applyOn:(UIViewController<RNNRootViewProtocol> *)viewController {
	[self.topBar applyOn:viewController];
	[self.bottomTabs applyOn:viewController];
	[self.topTab applyOn:viewController];
	[self.bottomTab applyOn:viewController];
	[self.sideMenu applyOn:viewController];
	[self.overlay applyOn:viewController];
	[self applyOtherOptionsOn:viewController];
	
	[viewController optionsUpdated];
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
		backgroundImageView.image = [self.backgroundImage isKindOfClass:[UIImage class]] ? (UIImage*)self.backgroundImage : [RCTConvert UIImage:self.backgroundImage];
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
	
	if (self.rootBackgroundImage) {
		UIImageView* backgroundImageView = (viewController.navigationController.view.subviews.count > 0) ? viewController.navigationController.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:viewController.view.bounds];
			[viewController.navigationController.view insertSubview:backgroundImageView atIndex:0];
		}

		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = [self.rootBackgroundImage isKindOfClass:[UIImage class]] ? (UIImage*)self.rootBackgroundImage : [RCTConvert UIImage:self.rootBackgroundImage];
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
	
	if (self.statusBarStyle) {
		[viewController setNeedsStatusBarAppearanceUpdate];
	}
    
    [self applyModalOptions:viewController];
}

- (void)applyModalOptions:(UIViewController*)viewController {
    if (self.modalPresentationStyle) {
        viewController.modalPresentationStyle = [RCTConvert UIModalPresentationStyle:self.modalPresentationStyle];
    }
    if (self.modalTransitionStyle) {
        viewController.modalTransitionStyle = [RCTConvert UIModalTransitionStyle:self.modalTransitionStyle];
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
