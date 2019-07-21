#import "UIView+Utils.h"


@implementation UIView (Utils)
- (UIView *)findChildByClass:(id)clazz {
    for (UIView *child in [self subviews]) {
        if ([child isKindOfClass:clazz]) return child;
    }
    return nil;
}

@end