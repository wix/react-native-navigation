#import "UIView+Utils.h"

#ifdef RCT_NEW_ARCH_ENABLED
#import <React/RCTImageComponentView.h>
#import <React/RCTParagraphComponentView.h>
#else
#import <React/RCTImageView.h>
#import <React/RCTTextView.h>
#endif

@implementation UIView (Utils)

- (UIView *)findChildByClass:(id)clazz {
    for (UIView *child in [self subviews]) {
        if ([child isKindOfClass:clazz])
            return child;
    }
    return nil;
}

- (ViewType)viewType {
#ifdef RCT_NEW_ARCH_ENABLED
	if ([self isKindOfClass:[RCTImageComponentView class]]) {
#else
    if ([self isKindOfClass:[RCTImageView class]]) {
#endif
        return ViewTypeImage;
#ifdef RCT_NEW_ARCH_ENABLED
	} else if ([self isKindOfClass:[RCTParagraphComponentView class]]) {
#else
    } else if ([self isKindOfClass:[RCTTextView class]]) {
#endif
        return ViewTypeText;
    } else if ([self isKindOfClass:[UIImageView class]]) {
        return ViewTypeUIImage;
    }

    return ViewTypeOther;
}

- (void)stopMomentumScrollViews {
    if ([self isKindOfClass:[UIScrollView class]]) {
        UIScrollView *scrollView = (UIScrollView *)self;
        [scrollView setContentOffset:scrollView.contentOffset animated:NO];
    }
    for (UIView *subview in self.subviews) {
        [subview stopMomentumScrollViews];
    }
}

- (void)setCornerRadius:(CGFloat)cornerRadius {
    self.layer.cornerRadius = cornerRadius;
}

@end
