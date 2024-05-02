#import "UITabBar+utils.h"
#import "UITabBarController+RNNUtils.h"
#import "UIView+Utils.h"

@implementation UITabBarController (RNNUtils)
- (UIView *)getTabView:(int)tabIndex {
    return [[self tabBar] tabBarItemViewAtIndex:tabIndex];
}

- (UIView *)getTabIcon:(int)tabIndex {
    UIView *tab = [self getTabView:tabIndex];
    return [tab findChildByClass:[UIImageView class]];
}

- (NSArray *)deselectedViewControllers {
    NSMutableArray *childViewControllers =
        [NSMutableArray arrayWithArray:self.childViewControllers];
    [childViewControllers removeObject:self.selectedViewController];
    return [NSArray arrayWithArray:childViewControllers];
}

@end
