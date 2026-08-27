#import "UITabBar+utils.h"
#import <objc/runtime.h>

#define BADGE_OFFSET 0.2
#define IMAGE_VIEW_TAG 1

typedef void (*UITabBarButton_layoutSubviews__IMP)(id, SEL);
static UITabBarButton_layoutSubviews__IMP original_UITabBarButton_layoutSubviews;

@implementation UITabBar (utils)

- (UIView *)tabBarItemViewAtIndex:(NSUInteger)index {
    return [self.items[index] valueForKey:@"view"];
}

- (void)centerTabItems {
    [self removeTabBarItemTitles];
    [self swizzleUITabBarButton];
}

- (void)removeTabBarItemTitles {
    for (UITabBarItem *item in self.items) {
        item.title = nil;
    }
}

- (void)swizzleUITabBarButton {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
      [[self class] swizzleUITabBarButtonLayoutSubviews];
    });
}

+ (void)swizzleUITabBarButtonLayoutSubviews {
    Class UITabBarButtonClass = NSClassFromString(@"UITabBarButton");
    if (!UITabBarButtonClass) return;

    SEL layoutSubviewsSEL = @selector(layoutSubviews);
    Method layoutSubviewsMethod = class_getInstanceMethod(UITabBarButtonClass, layoutSubviewsSEL);

    SEL swizzleUITabBarButton_layoutSubviewsSEL = @selector(swizzleUITabBarButton_layoutSubviews);
    Method swizzleUITabBarButton_layoutSubviewsMethod =
        class_getInstanceMethod(self, swizzleUITabBarButton_layoutSubviewsSEL);
    if (!layoutSubviewsMethod || !swizzleUITabBarButton_layoutSubviewsMethod) return;

    original_UITabBarButton_layoutSubviews =
        (UITabBarButton_layoutSubviews__IMP)method_getImplementation(layoutSubviewsMethod);

    // Replace only the button's method, even if its implementation is inherited.
    class_replaceMethod(UITabBarButtonClass, layoutSubviewsSEL,
                        method_getImplementation(swizzleUITabBarButton_layoutSubviewsMethod),
                        method_getTypeEncoding(layoutSubviewsMethod));
}

- (void)swizzleUITabBarButton_layoutSubviews {
    original_UITabBarButton_layoutSubviews(self, @selector(layoutSubviews));
    for (UIView *subView in self.subviews) {
        if ([subView isKindOfClass:NSClassFromString(@"UITabBarSwappableImageView")]) {
            subView.center = CGPointMake(subView.center.x, subView.superview.frame.size.height / 2);
            subView.tag = IMAGE_VIEW_TAG;
        }

        if ([subView isKindOfClass:NSClassFromString(@"_UIBadgeView")]) {
            UIView *imageView = [subView.superview viewWithTag:IMAGE_VIEW_TAG];
            subView.frame =
                CGRectMake(subView.frame.origin.x,
                           (imageView.frame.origin.y + imageView.frame.size.height * BADGE_OFFSET) -
                               subView.frame.size.height / 2,
                           subView.frame.size.width, subView.frame.size.height);
        }
    }
}

@end
