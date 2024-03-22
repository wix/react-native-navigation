#import "RNNConstants.h"
#import "UIViewController+LayoutProtocol.h"

@implementation RNNConstants

+ (NSDictionary *)getConstants {
    return @{
        @"topBarHeight" : @([self topBarHeight]),
        @"statusBarHeight" : @([self statusBarHeight]),
        @"bottomTabsHeight" : @([self bottomTabsHeight])
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
