#import "UIViewController+RNNOptions.h"
#import <React/RCTRootView.h>
#import "RNNBottomTabOptions.h"
#import "RNNNavigationOptions.h"
#import "RNNBackButtonOptions.h"

#define kStatusBarAnimationDuration 0.35
const NSInteger BLUR_STATUS_TAG = 78264801;

@implementation UIViewController (RNNOptions)

- (void)setBackgroundImage:(UIImage *)backgroundImage {
	if (backgroundImage) {
		UIImageView* backgroundImageView = (self.view.subviews.count > 0) ? self.view.subviews[0] : nil;
		if (![backgroundImageView isKindOfClass:[UIImageView class]]) {
			backgroundImageView = [[UIImageView alloc] initWithFrame:self.view.bounds];
			[self.view insertSubview:backgroundImageView atIndex:0];
		}

		backgroundImageView.layer.masksToBounds = YES;
		backgroundImageView.image = backgroundImage;
		[backgroundImageView setContentMode:UIViewContentModeScaleAspectFill];
	}
}
#if !TARGET_OS_TV
- (void)setSearchBarWithPlaceholder:(NSString *)placeholder
		 hideNavBarOnFocusSearchBar:(BOOL)hideNavBarOnFocusSearchBar {
	if (@available(iOS 11.0, *)) {
		if (!self.navigationItem.searchController) {
			UISearchController *search = [[UISearchController alloc]initWithSearchResultsController:nil];
			search.dimsBackgroundDuringPresentation = NO;
			if ([self conformsToProtocol:@protocol(UISearchResultsUpdating)]) {
				[search setSearchResultsUpdater:((UIViewController <UISearchResultsUpdating> *) self)];
			}
			search.searchBar.delegate = (id<UISearchBarDelegate>)self;
			if (placeholder) {
				search.searchBar.placeholder = placeholder;
			}
			search.hidesNavigationBarDuringPresentation = hideNavBarOnFocusSearchBar;
			self.navigationItem.searchController = search;
			[self.navigationItem setHidesSearchBarWhenScrolling:NO];

			// Fixes #3450, otherwise, UIKit will infer the presentation context to be the root most view controller
			self.definesPresentationContext = YES;
		}
	}
}
#endif
#if !TARGET_OS_TV
- (void)setSearchBarHiddenWhenScrolling:(BOOL)searchBarHidden {
	if (@available(iOS 11.0, *)) {
		self.navigationItem.hidesSearchBarWhenScrolling = searchBarHidden;
	}
}
#endif
- (void)setNavigationItemTitle:(NSString *)title {
	self.navigationItem.title = title;
}

- (void)setDrawBehindTopBar:(BOOL)drawBehind {
	if (drawBehind) {
		self.edgesForExtendedLayout |= UIRectEdgeTop;
	} else {
		self.edgesForExtendedLayout &= ~UIRectEdgeTop;
	}
    
    if (self.isViewLoaded) {
        [self.view setNeedsLayout];
        [self.view layoutIfNeeded];
    }
}

- (void)setDrawBehindTabBar:(BOOL)drawBehindTabBar {
	if (drawBehindTabBar) {
		self.edgesForExtendedLayout |= UIRectEdgeBottom;
	} else {
		self.edgesForExtendedLayout &= ~UIRectEdgeBottom;
	}
    
    if (self.isViewLoaded) {
        [self.view setNeedsLayout];
        [self.view layoutIfNeeded];
    }
}

- (void)setTabBarItemBadge:(NSString *)badge {
    UITabBarItem *tabBarItem = self.tabBarItem;

    if ([badge isKindOfClass:[NSNull class]] || [badge isEqualToString:@""]) {
        tabBarItem.badgeValue = nil;
    } else {
        tabBarItem.badgeValue = badge;
        [[self.tabBarController.tabBar viewWithTag:tabBarItem.tag] removeFromSuperview];
        tabBarItem.tag = -1;
    }
}

- (void)setTabBarItemBadgeColor:(UIColor *)badgeColor {
	if (@available(iOS 10.0, *)) {
		self.tabBarItem.badgeColor = badgeColor;
	}
}

- (void)setStatusBarStyle:(NSString *)style animated:(BOOL)animated {
	if (animated) {
		[UIView animateWithDuration:[self statusBarAnimationDuration:animated] animations:^{
			[self setNeedsStatusBarAppearanceUpdate];
		}];
	} else {
		[self setNeedsStatusBarAppearanceUpdate];
	}
}
#if !TARGET_OS_TV
- (void)setTopBarPrefersLargeTitle:(BOOL)prefersLargeTitle {
	if (@available(iOS 11.0, *)) {
		if (prefersLargeTitle) {
			self.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeAlways;
		} else {
			self.navigationItem.largeTitleDisplayMode = UINavigationItemLargeTitleDisplayModeNever;
		}
	}
}
- (void)setStatusBarBlur:(BOOL)blur {
	UIView* curBlurView = [self.view viewWithTag:BLUR_STATUS_TAG];
	if (blur) {
		if (!curBlurView) {
			UIVisualEffectView *blur = [[UIVisualEffectView alloc] initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
            #if !TARGET_OS_TV
			blur.frame = [[UIApplication sharedApplication] statusBarFrame];
            #endif
			blur.tag = BLUR_STATUS_TAG;
			[self.view insertSubview:blur atIndex:0];
		}
	} else {
		if (curBlurView) {
			[curBlurView removeFromSuperview];
		}
	}
}
#endif




- (void)setBackgroundColor:(UIColor *)backgroundColor {
	UIBlurEffect *blurEffect = [UIBlurEffect effectWithStyle:UIBlurEffectStyleDark];
    UIVisualEffectView *blurEffectView = [[UIVisualEffectView alloc] initWithEffect:blurEffect];
    self.view.backgroundColor =  [UIColor clearColor];
    blurEffectView.frame =  self.view.bounds;
    blurEffectView.autoresizingMask = UIViewAutoresizingFlexibleWidth | UIViewAutoresizingFlexibleHeight;
    [self.view  insertSubview:blurEffectView atIndex:0]; 
    self.view.layer.masksToBounds = true;
}
#if !TARGET_OS_TV
- (void)setBackButtonVisible:(BOOL)visible {
	self.navigationItem.hidesBackButton = !visible;
}
#endif

- (CGFloat)statusBarAnimationDuration:(BOOL)animated {
	return animated ? kStatusBarAnimationDuration : CGFLOAT_MIN;
}

- (BOOL)isModal {
	if([self presentingViewController])
		return YES;
	if([[[self navigationController] presentingViewController] presentedViewController] == [self navigationController])
		return YES;
	if([[[self tabBarController] presentingViewController] isKindOfClass:[UITabBarController class]])
		return YES;

	return NO;
}

- (void)setInterceptTouchOutside:(BOOL)interceptTouchOutside {
	if ([self.view isKindOfClass:[RCTRootView class]]) {
		RCTRootView* rootView = (RCTRootView*)self.view;
		rootView.passThroughTouches = !interceptTouchOutside;
	}
}

@end
