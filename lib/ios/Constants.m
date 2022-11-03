#import "Constants.h"
#import "UIViewController+LayoutProtocol.h"

@implementation Constants

+ (NSDictionary *)getConstants {
    return @{
        @"topBarHeight" : @([self topBarHeight]),
#if !TARGET_OS_TV
        @"statusBarHeight" : @([self statusBarHeight]),
#endif
        @"bottomTabsHeight" : @([self bottomTabsHeight])
    };
}

+ (CGFloat)topBarHeight {
    return [RCTPresentedViewController() getTopBarHeight];
}

#if !TARGET_OS_TV
+ (CGFloat)statusBarHeight {
    return [UIApplication sharedApplication].statusBarFrame.size.height;
}
#endif

+ (CGFloat)bottomTabsHeight {
    return [UIApplication.sharedApplication.delegate.window.rootViewController getBottomTabsHeight];
}

@end
