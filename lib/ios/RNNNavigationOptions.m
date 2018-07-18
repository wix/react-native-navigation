#import "RNNNavigationOptions.h"
#import <React/RCTConvert.h>
#import "RNNNavigationController.h"
#import "RNNTabBarController.h"
#import "RNNTopBarOptions.h"
#import "RNNSideMenuController.h"
#import "RNNRootViewController.h"
#import "RNNSplitViewController.h"
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
	[self mergeOptions:_defaultOptions overrideOptions:NO];
	[self.topBar applyOn:viewController];
	[self.bottomTabs applyOn:viewController];
	[self.topTab applyOn:viewController];
	[self.bottomTab applyOn:viewController];
	[self.sideMenu applyOn:viewController];
	[self.overlay applyOn:viewController];
	[self.statusBar applyOn:viewController];
	[self.layout applyOn:viewController];
	[self applyOtherOptionsOn:viewController];
	
	[viewController.getLeafViewController optionsUpdated];
}

- (void)applyOtherOptionsOn:(UIViewController*)viewController {
	if (self.popGesture) {
		viewController.navigationController.interactivePopGestureRecognizer.enabled = [self.popGesture boolValue];
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
	
	[self applyModalOptions:viewController];
}

- (void)applyModalOptions:(UIViewController*)viewController {
	if (self.modalPresentationStyle) {
		viewController.modalPresentationStyle = [RCTConvert UIModalPresentationStyle:self.modalPresentationStyle];
		[viewController.view setBackgroundColor:[UIColor clearColor]];
	}
	if (self.modalTransitionStyle) {
		viewController.modalTransitionStyle = [RCTConvert UIModalTransitionStyle:self.modalTransitionStyle];
	}
}


@end
