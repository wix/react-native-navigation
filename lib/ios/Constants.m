#import "Constants.h"

@implementation Constants

+ (NSDictionary *)getConstants {
	return @{@"topBarHeight": @([self topBarHeight]), @"statusBarHeight": @([self statusBarHeight]), @"bottomTabsHeight": @([self bottomTabsHeight])};
}

+ (CGFloat)topBarHeight {
	return UIApplication.sharedApplication.delegate.window.rootViewController.navigationController.navigationBar.frame.size.height;
}


+ (CGFloat)statusBarHeight {
	#if TARGET_OS_TV
	return 0;
	#else
	return [UIApplication sharedApplication].statusBarFrame.size.height;
	#endif
}


+ (CGFloat)bottomTabsHeight {
	return CGRectGetHeight(((UITabBarController *)((UIWindow *)(UIApplication.sharedApplication.windows[0])).rootViewController).tabBar.frame);
}

@end
