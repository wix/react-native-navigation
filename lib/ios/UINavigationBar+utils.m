#import "UINavigationBar+utils.h"

const NSInteger TOP_BAR_TRANSPARENT_TAG = 78264803;

@implementation UINavigationBar (utils)

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

- (void)rnn_setBackgroundColor:(UIColor *)color {
    CGFloat bgColorAlpha = CGColorGetAlpha(color.CGColor);
    
    if (color && bgColorAlpha == 0.0) {
        self.translucent = YES;
        [self setBackgroundColorTransparent];
    } else {
        self.translucent = NO;
        [self setBackgroundColor:color];
    }
}

- (void)setBackgroundColor:(UIColor *)color {
    if (@available(iOS 13.0, *)) {
        [self configureWithDefaultBackground];
        [self getNavigaitonBarStandardAppearance].backgroundColor = color;
        [self getNavigaitonBarCompactAppearance].backgroundColor = color;
        [self getNavigaitonBarScrollEdgeAppearance].backgroundColor = color;
    } else {
        [super setBackgroundColor:color];
        [self removeTransparentView];
    }
}

- (void)setBackgroundColorTransparent {
    if (@available(iOS 13.0, *)) {
        [self configureWithTransparentBackground];
    } else {
        if (![self viewWithTag:TOP_BAR_TRANSPARENT_TAG]){
            UIView *transparentView = [[UIView alloc] initWithFrame:CGRectZero];
            transparentView.backgroundColor = [UIColor clearColor];
            transparentView.tag = TOP_BAR_TRANSPARENT_TAG;
            [self insertSubview:transparentView atIndex:0];
        }
        
        [self setBackgroundColor:[UIColor clearColor]];
        self.shadowImage = [UIImage new];
        [self setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
    }
}

- (void)removeTransparentView {
    UIView *transparentView = [self viewWithTag:TOP_BAR_TRANSPARENT_TAG];
    if (transparentView){
        [transparentView removeFromSuperview];
    }
}

- (void)configureWithTransparentBackground {
    if (@available(iOS 13.0, *)) {
        [[self getNavigaitonBarStandardAppearance] configureWithTransparentBackground];
        [[self getNavigaitonBarCompactAppearance] configureWithTransparentBackground];
        [[self getNavigaitonBarScrollEdgeAppearance] configureWithTransparentBackground];
    }
}

- (void)configureWithDefaultBackground {
    if (@available(iOS 13.0, *)) {
        [[self getNavigaitonBarStandardAppearance] configureWithDefaultBackground];
        [[self getNavigaitonBarCompactAppearance] configureWithDefaultBackground];
        [[self getNavigaitonBarScrollEdgeAppearance] configureWithDefaultBackground];
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
