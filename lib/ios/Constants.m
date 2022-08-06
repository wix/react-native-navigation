#import "Constants.h"
#import "UIViewController+LayoutProtocol.h"
static NSString *const RCTSetNonceValueNotification = @"RCTSetNonceValueNotification";

@implementation Constants

+ (NSDictionary *)getConstants {
    return @{@"topBarHeight": @([self topBarHeight]), @"statusBarHeight": @([self statusBarHeight]), @"nonceString": [self nonce],  @"bottomTabsHeight": @([self bottomTabsHeight])};
}

+ (CGFloat)topBarHeight {
	return [RCTPresentedViewController() getTopBarHeight];
}
+ (NSString *)nonce {
    return [RCTPresentedViewController() getNonce];
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
