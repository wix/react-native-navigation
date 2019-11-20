#import "UITabBar+centered.h"
#import <objc/runtime.h>

#define BADGE_OFFSET 0.2
#define IMAGE_VIEW_TAG 1

typedef void (*UITabBarButton_layoutSubviews__IMP)(void);
static UITabBarButton_layoutSubviews__IMP original_UITabBarButton_layoutSubviews;

@implementation UITabBar (centered)

- (void)centerTabItems {
	static dispatch_once_t onceToken;
	dispatch_once(&onceToken, ^{
		[[self class] rnn_extendsUITabBarButtonLayoutSubviews];
	});
	
	for (UITabBarItem* item in self.items) {
		item.title = nil;
	}
}

- (void)rnn_UITabBarButton_layoutSubviews {
	original_UITabBarButton_layoutSubviews();
	for (UIView *subView in self.subviews) {
		if ([subView isKindOfClass:NSClassFromString(@"UITabBarSwappableImageView")]) {
			subView.center = CGPointMake(subView.center.x, subView.superview.frame.size.height / 2);
			subView.tag = IMAGE_VIEW_TAG;
		}
		
		if ([subView isKindOfClass:NSClassFromString(@"_UIBadgeView")]) {
			UIView* imageView = [subView.superview viewWithTag:IMAGE_VIEW_TAG];
			subView.frame = CGRectMake(subView.frame.origin.x, (imageView.frame.origin.y + imageView.frame.size.height * BADGE_OFFSET) - subView.frame.size.height / 2, subView.frame.size.width, subView.frame.size.height);
		}
	}
}

+ (void)rnn_extendsUITabBarButtonLayoutSubviews {
	Class UITabBarButtonClass = NSClassFromString(@"UITabBarButton");
	
	SEL layoutSubviewsSEL = @selector(layoutSubviews);
	Method layoutSubviewsMethod = class_getInstanceMethod(UITabBarButtonClass, layoutSubviewsSEL);
	IMP layoutSubviewsIMP = method_getImplementation(layoutSubviewsMethod);
	
	original_UITabBarButton_layoutSubviews = layoutSubviewsIMP;
	
	SEL rnn_UITabBarButton_layoutSubviewsSEL = @selector(rnn_UITabBarButton_layoutSubviews);
	Method rnn_UITabBarButton_layoutSubviewsMethod = class_getInstanceMethod(self, rnn_UITabBarButton_layoutSubviewsSEL);

	method_exchangeImplementations(layoutSubviewsMethod, rnn_UITabBarButton_layoutSubviewsMethod);
}

@end
