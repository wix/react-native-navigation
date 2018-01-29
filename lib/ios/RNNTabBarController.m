
#import "RNNTabBarController.h"
#import "RNNRootViewController.h"
#define kTabBarHiddenDuration 0.3

@implementation RNNTabBarController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.selectedViewController.supportedInterfaceOrientations;
}

- (void)setTabBarHidden:(BOOL)hidden animated:(BOOL)animated {
	CGRect frame = self.tabBar.frame;
	CGFloat height = frame.size.height;
	CGFloat offsetY = (hidden ? self.view.frame.size.height : self.view.frame.size.height-height);
	frame.origin.y = offsetY;
	NSTimeInterval duration = animated ? kTabBarHiddenDuration : 0.0;
	
	[UIView animateWithDuration:duration animations:^{
		self.tabBar.frame = frame;
	}];
}

- (void)setSelectedIndexByComponentID:(NSString *)componentID {
	for (id child in self.childViewControllers) {
		RNNRootViewController* vc = child;
		if ([child isKindOfClass:[UINavigationController class]]) {
			vc = ((UINavigationController *)child).childViewControllers.firstObject;
		}
		if ([vc.componentId isEqualToString:componentID]) {
			[self setSelectedIndex:[self.childViewControllers indexOfObject:child]];
		}
	}
}

- (BOOL)isCustomTransitioned {
	return NO;
}

- (BOOL)isAnimated {
	return YES;
}

- (NSString *)componentId {
	return ((UIViewController<RNNRootViewProtocol>*)self.selectedViewController).componentId;
}

@end
