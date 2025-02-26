#import "Constants.h"
#import "UIViewController+LayoutProtocol.h"

@implementation Constants

+ (NSDictionary *)getConstants {
    return @{
        @"topBarHeight" : @([self topBarHeight]),
        @"statusBarHeight" : @([self statusBarHeight]),
        @"bottomTabsHeight" : @([self bottomTabsHeight])
    };
}

#ifdef RCT_NEW_ARCH_ENABLED
+ (JS::NativeRNNTurboModule::Constants::Builder::Input)getTurboConstants {
	JS::NativeRNNTurboModule::Constants::Builder::Input input = {
		[self topBarHeight],
		[self statusBarHeight],
		[self bottomTabsHeight]
	};

	return input;
}
#endif

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
