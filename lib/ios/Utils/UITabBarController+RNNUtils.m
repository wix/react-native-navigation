#import "UITabBarController+RNNUtils.h"
#import "UIView+Utils.h"


@implementation UITabBarController (RNNUtils)
- (UIView *)getTabView:(int)tabIndex {
    int index = 0;
    for (UIView *view in [[self tabBar] subviews]) {
        if ([NSStringFromClass([view class]) isEqualToString:@"UITabBarButton"]) {
            if (index == tabIndex) return view;
            index++;
        }
    }
    return nil;
}

- (UIView *)getTabIcon:(int)tabIndex {
    UIView *tab = [self getTabView:tabIndex];
    return [tab findChildByClass:[UIImageView class]];
}
@end