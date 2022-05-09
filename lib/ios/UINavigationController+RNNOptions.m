#import "RNNFontAttributesCreator.h"
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
        CGRect statusBarFrame = [[UIApplication sharedApplication] statusBarFrame];
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

- (void)setBackButtonTestID:(NSString *)testID {
    UIView *navigationBarContentView =
        [self.navigationBar findChildByClass:NSClassFromString(@"_UINavigationBarContentView")];
    UIView *barButton =
        [navigationBarContentView findChildByClass:NSClassFromString(@"_UIButtonBarButton")];
    if (barButton)
        barButton.accessibilityIdentifier = testID;
}

- (CGFloat)getTopBarHeight {
    return self.navigationBar.frame.origin.y + self.navigationBar.frame.size.height;
}

@end
