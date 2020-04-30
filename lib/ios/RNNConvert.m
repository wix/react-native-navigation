#import "RNNConvert.h"

@implementation RNNConvert

+ (UIModalPresentationStyle)defaultModalPresentationStyle {
    if (@available(iOS 13.0, *)) {
        return UIModalPresentationAutomatic;
    } else {
        return UIModalPresentationOverFullScreen;
    }
}
#if TARGET_OS_TV
RCT_ENUM_CONVERTER(UIModalTransitionStyle,
                   (@{@"coverVertical": @(UIModalTransitionStyleCoverVertical),
                      @"crossDissolve": @(UIModalTransitionStyleCrossDissolve),
                      }), UIModalTransitionStyleCoverVertical, integerValue)

RCT_ENUM_CONVERTER(UIModalPresentationStyle,
                   (@{@"fullScreen": @(UIModalPresentationFullScreen),
                      @"currentContext": @(UIModalPresentationCurrentContext),
                      @"custom": @(UIModalPresentationCustom),
                      @"overFullScreen": @(UIModalPresentationOverFullScreen),
                      @"overCurrentContext": @(UIModalPresentationOverCurrentContext),
                      @"none": @(UIModalPresentationNone)
                      }), UIModalPresentationFullScreen, integerValue)
#else

RCT_ENUM_CONVERTER(UIModalTransitionStyle,
                   (@{@"coverVertical": @(UIModalTransitionStyleCoverVertical),
                      @"flipHorizontal": @(UIModalTransitionStyleFlipHorizontal),
                      @"crossDissolve": @(UIModalTransitionStyleCrossDissolve),
                      @"partialCurl": @(UIModalTransitionStylePartialCurl)
                   }), UIModalTransitionStyleCoverVertical, integerValue)

#endif

@end
