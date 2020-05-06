#import "Constants.h"
#import "UIViewController+LayoutProtocol.h"

@implementation Constants

+ (NSDictionary *)getConstants {
	return @{@"topBarHeight": @([self topBarHeight]), @"statusBarHeight": @([self statusBarHeight]), @"bottomTabsHeight": @([self bottomTabsHeight])};
}

+ (CGFloat)topBarHeight {
	return [RCTPresentedViewController() getTopBarHeight];
}

+ (CGFloat)statusBarHeight {
	#if TARGET_OS_TV
    return 0;
    #else
    return [UIApplication sharedApplication].statusBarFrame.size.height;       
    #endif
}

+ (CGFloat)bottomTabsHeight {
	return [UIApplication.sharedApplication.delegate.window.rootViewController getBottomTabsHeight];
}

@end
