#import "RNNFontAttributesCreator.h"
#import "RNNSceneUtils.h"
#import "UINavigationController+RNNOptions.h"
#import "UIView+Utils.h"

const NSInteger BLUR_TOPBAR_TAG = 78264802;

@implementation UINavigationController (RNNOptions)

- (void)setRootBackgroundImage:(UIImage *)backgroundImage {
    UIImageView *backgroundImageView = self.view.subviews.firstObject;
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

- (void)setBarStyle:(UIBarStyle)barStyle {
    self.navigationBar.barStyle = barStyle;
}

- (void)setNavigationBarBlur:(BOOL)blur {
    if (blur && ![self.navigationBar viewWithTag:BLUR_TOPBAR_TAG]) {
        [self.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
        self.navigationBar.shadowImage = [UIImage new];
        UIVisualEffectView *blur = [[UIVisualEffectView alloc]
            initWithEffect:[UIBlurEffect effectWithStyle:UIBlurEffectStyleLight]];
        CGRect statusBarFrame = RNNStatusBarFrame();
        blur.frame =
            CGRectMake(0, -1 * statusBarFrame.size.height, self.navigationBar.frame.size.width,
                       self.navigationBar.frame.size.height + statusBarFrame.size.height);
        blur.userInteractionEnabled = NO;
        blur.tag = BLUR_TOPBAR_TAG;
        [self.navigationBar insertSubview:blur atIndex:0];
        [self.navigationBar sendSubviewToBack:blur];
    } else {
        UIView *blur = [self.navigationBar viewWithTag:BLUR_TOPBAR_TAG];
        if (blur) {
            [self.navigationBar setBackgroundImage:nil forBarMetrics:UIBarMetricsDefault];
            self.navigationBar.shadowImage = nil;
            [blur removeFromSuperview];
        }
    }
}

- (void)setNavigationBarClipsToBounds:(BOOL)clipsToBounds {
    self.navigationBar.clipsToBounds = clipsToBounds;
}

- (UIView *)rnn_findBackButtonView {
    return [self rnn_findNavigationBarBackButtonView] ?: [self rnn_findVerticalBarBackButtonView];
}

- (BOOL)rnn_isInVerticalBar {
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= 270100
    if (@available(iOS 27.1, *)) {
        return self.traitCollection.verticalBarEdge != UIVerticalBarEdgeUnspecified;
    }
#endif
    return NO;
}

// iPhone Duo: the vertical bar hosts the back button outside the navigation bar, and shows only
// the top stack's back button.
- (UIView *)rnn_findVerticalBarBackButtonView {
    if (![self rnn_isInVerticalBar] || self.viewControllers.count < 2)
        return nil;

    SEL isBackButtonSelector = NSSelectorFromString(@"isBackButton");
    return [self.view.window findDescendantByClass:NSClassFromString(@"_UIButtonBarButton")
                                       passingTest:^BOOL(UIView *view) {
                                         return [view respondsToSelector:isBackButtonSelector] &&
                                                [[view valueForKey:@"backButton"] boolValue];
                                       }];
}

- (UIView *)rnn_findNavigationBarBackButtonView {
    UIView *contentView =
        [self.navigationBar findChildByClass:NSClassFromString(@"_UINavigationBarContentView")];
    if (!contentView) {
        contentView =
            [self.navigationBar findChildByClass:NSClassFromString(@"UIKit.NavigationBarContentView")];
    }
    if (!contentView)
        return nil;

    Class buttonClass = NSClassFromString(@"_UIButtonBarButton");

    return [contentView findDescendantByClass:buttonClass
                                 passingTest:^BOOL(UIView *view) {
        if ([view respondsToSelector:NSSelectorFromString(@"isBackButton")]) {
            return [[view valueForKey:@"backButton"] boolValue];
        }
        return YES;
    }];
}

- (void)rnn_applyTestID:(NSString *)testID toBackButtonView:(UIView *)barButton {
    barButton.accessibilityIdentifier = testID;
    barButton.isAccessibilityElement = YES;
}

- (void)setBackButtonTestID:(NSString *)testID {
    if (!testID)
        return;

    UIView *barButton = [self rnn_findBackButtonView];
    if (barButton) {
        [self rnn_applyTestID:testID toBackButtonView:barButton];
    } else {
        __weak UINavigationController *weakSelf = self;
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.15 * NSEC_PER_SEC)),
                       dispatch_get_main_queue(), ^{
            UINavigationController *nc = weakSelf;
            if (!nc)
                return;
            UIView *btn = [nc rnn_findBackButtonView];
            if (btn) {
                [nc rnn_applyTestID:testID toBackButtonView:btn];
            }
        });
    }
}

- (CGFloat)getTopBarHeight {
    return self.navigationBar.frame.origin.y + self.navigationBar.frame.size.height;
}

@end
