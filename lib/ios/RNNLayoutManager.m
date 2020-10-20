#import "RNNLayoutManager.h"
#import "RNNLayoutProtocol.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNLayoutManager

+ (UIViewController *)findComponentForId:(NSString *)componentId {
	for (UIWindow *window in UIApplication.sharedApplication.windows) {
		UIViewController *result = [self findChildComponentForParent:window.rootViewController forId:componentId];
		if (result) {
			return result;
		}
	}
	
	return nil;
}

+ (UIViewController *)findChildComponentForParent:(UIViewController *)parentViewController forId:(NSString *)componentId {
	if ([parentViewController.layoutInfo.componentId isEqualToString:componentId]) {
		return parentViewController;
	}
	
	if (parentViewController.presentedViewController) {
		UIViewController *modalResult = [self findChildComponentForParent:parentViewController.presentedViewController forId:componentId];
		if (modalResult) {
			return modalResult;
		}
	}
	
	for (UIViewController *childVC in parentViewController.childViewControllers) {
		UIViewController *result = [self findChildComponentForParent:childVC forId:componentId];
		if (result) {
			return result;
		}
	}
	
	return nil;
}

@end
