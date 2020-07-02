#import "UIView+Utils.h"
#import <React/RCTImageView.h>
#import <React/RCTTextView.h>

@implementation UIView (Utils)

- (UIView *)findChildByClass:(id)clazz {
    for (UIView *child in [self subviews]) {
        if ([child isKindOfClass:clazz]) return child;
    }
    return nil;
}

- (ViewType)viewType {
    if ([self isKindOfClass:[RCTImageView class]]) {
        return ViewTypeImage;
    } else if ([self isKindOfClass:[RCTTextView class]]) {
        return ViewTypeText;
    }
    
    return ViewTypeOther;
}

@end
