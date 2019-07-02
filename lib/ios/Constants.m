#import "Constants.h"
#import "UIViewController+LayoutProtocol.h"

@implementation Constants

+ (NSDictionary *)getConstants {
	return @{@"topBarHeight": @([self topBarHeight]), @"statusBarHeight": @([self statusBarHeight]), @"bottomTabsHeight": @([self bottomTabsHeight])};
}

+ (CGFloat)topBarHeight {
	return [UIApplication .sharedApplication.delegate.window.rootViewController getTopBarHeight];
}

+ (CGFloat)statusBarHeight {
	return [UIApplication sharedApplication].statusBarFrame.size.height;
}

+ (CGFloat)bottomTabsHeight {
	@try {
		return CGRectGetHeight(((UITabBarController *) UIApplication.sharedApplication.windows[0].rootViewController).tabBar.frame);
	} @catch (NSException *exception) {
		return 0;
	}
}

@end
