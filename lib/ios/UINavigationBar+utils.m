#import "UINavigationBar+utils.h"

const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@implementation UINavigationBar (utils)

- (void)rnn_setBackgroundColor:(UIColor *)color {
    CGFloat bgColorAlpha = CGColorGetAlpha(color.CGColor);
    
    if (color && bgColorAlpha == 0.0) {
        [self rnn_setBackgroundColorTransparent];
    } else {
        [self removeTransparentView];
        if (@available(iOS 13.0, *)) {
            [self getNavigaitonBarStandardAppearance].backgroundColor = color;
            [self getNavigaitonBarCompactAppearance].backgroundColor = color;
            [self getNavigaitonBarScrollEdgeAppearance].backgroundColor = color;
        }
        self.barTintColor = color;
    }
}

- (void)rnn_setBackgroundColorTransparent {
    if (![self viewWithTag:TOP_BAR_TRANSPARENT_TAG]){
        UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
        transparentView.backgroundColor = [UIColor clearColor];
        transparentView.tag = TOP_BAR_TRANSPARENT_TAG;
        [self insertSubview:transparentView atIndex:0];
    }
    
    self.translucent = YES;
    [self setBackgroundColor:[UIColor clearColor]];
    self.shadowImage = [UIImage new];
    [self setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
    if (@available(iOS 13.0, *)) {
        UINavigationBarAppearance *standardAppearance = [self getNavigaitonBarStandardAppearance];
        standardAppearance.backgroundColor = [UIColor clearColor];
        standardAppearance.shadowImage =  [UIImage new];
        standardAppearance.backgroundImage = [UIImage new];

        UINavigationBarAppearance *compactAppearance = [self getNavigaitonBarCompactAppearance];
        compactAppearance.backgroundColor = [UIColor clearColor];
        compactAppearance.shadowImage =  [UIImage new];
        compactAppearance.backgroundImage = [UIImage new];

        UINavigationBarAppearance *scrollEdgeAppearance = [self getNavigaitonBarScrollEdgeAppearance];
        scrollEdgeAppearance.backgroundColor = [UIColor clearColor];
        scrollEdgeAppearance.shadowImage =  [UIImage new];
        scrollEdgeAppearance.backgroundImage = [UIImage new];
    }
}

- (void)removeTransparentView {
    UIView *transparentView = [self viewWithTag:TOP_BAR_TRANSPARENT_TAG];
    if (transparentView){
        [transparentView removeFromSuperview];
    }
}

- (void)rnn_setBackIndicatorImage:(UIImage *)image {
    if (@available(iOS 13.0, *)) {
        [[self getNavigaitonBarStandardAppearance] setBackIndicatorImage:image transitionMaskImage:image];
        [[self getNavigaitonBarCompactAppearance] setBackIndicatorImage:image transitionMaskImage:image];
        [[self getNavigaitonBarScrollEdgeAppearance] setBackIndicatorImage:image transitionMaskImage:image];
    } else {
        [self setBackIndicatorImage:image];
        [self setBackIndicatorTransitionMaskImage:image];
    }
}

- (UINavigationBarAppearance*)getNavigaitonBarStandardAppearance  API_AVAILABLE(ios(13.0)) {
    if (!self.standardAppearance) {
        self.standardAppearance = [UINavigationBarAppearance new];
    }
    return self.standardAppearance;
}

- (UINavigationBarAppearance*)getNavigaitonBarCompactAppearance  API_AVAILABLE(ios(13.0)) {
    if (!self.compactAppearance) {
        self.compactAppearance = [UINavigationBarAppearance new];
    }
    return self.compactAppearance;
}

- (UINavigationBarAppearance*)getNavigaitonBarScrollEdgeAppearance  API_AVAILABLE(ios(13.0)) {
    if (!self.scrollEdgeAppearance) {
        self.scrollEdgeAppearance = [UINavigationBarAppearance new];
    }
    return self.scrollEdgeAppearance;
}

@end
