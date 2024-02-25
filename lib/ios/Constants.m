#import "Constants.h"
#import "UIViewController+LayoutProtocol.h"

@implementation Constants

+ (NSDictionary *)getConstants {
    return @{
        @"topBarHeight" : @([self topBarHeight]),
        @"statusBarHeight" : @([self statusBarHeight]),
        @"bottomTabsHeight" : @([self bottomTabsHeight]),
        @"navigationBarHeight" : @(0)
    };
}

+ (CGFloat)topBarHeight {
    return [RCTPresentedViewController() getTopBarHeight];
}

+ (CGFloat)statusBarHeight {
    return [UIApplication sharedApplication].statusBarFrame.size.height;
}

+ (CGFloat)bottomTabsHeight {
    return [UIApplication.sharedApplication.delegate.window.rootViewController getBottomTabsHeight];
}

@end
