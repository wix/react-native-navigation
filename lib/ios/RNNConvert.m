#import "RNNConvert.h"

@implementation RNNConvert

+ (UIModalPresentationStyle)defaultModalPresentationStyle {
    if (@available(iOS 13.0, *)) {
        return UIModalPresentationAutomatic;
    } else {
        return UIModalPresentationFullScreen;
    }
}

RCT_ENUM_CONVERTER(UIModalTransitionStyle, (@{
                       @"coverVertical" : @(UIModalTransitionStyleCoverVertical),
#if !TARGET_OS_TV
                       @"flipHorizontal" : @(UIModalTransitionStyleFlipHorizontal),
#endif
                       @"crossDissolve" : @(UIModalTransitionStyleCrossDissolve),
#if !TARGET_OS_TV
                       @"partialCurl" : @(UIModalTransitionStylePartialCurl)
#endif
                   }),
                   UIModalTransitionStyleCoverVertical, integerValue)

RCT_ENUM_CONVERTER(UIModalPresentationStyle, (@{
                       @"fullScreen" : @(UIModalPresentationFullScreen),
#if !TARGET_OS_TV
                       @"pageSheet" : @(UIModalPresentationPageSheet),
#endif
#if !TARGET_OS_TV
                       @"formSheet" : @(UIModalPresentationFormSheet),
#endif
                       @"currentContext" : @(UIModalPresentationCurrentContext),
                       @"custom" : @(UIModalPresentationCustom),
                       @"overFullScreen" : @(UIModalPresentationOverFullScreen),
                       @"overCurrentContext" : @(UIModalPresentationOverCurrentContext),
#if !TARGET_OS_TV
                       @"popover" : @(UIModalPresentationPopover),
#endif
                       @"none" : @(UIModalPresentationNone),
                       @"default" : @([RNNConvert defaultModalPresentationStyle])
                   }),
                   UIModalPresentationFullScreen, integerValue)

@end
