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
                       @"flipHorizontal" : @(UIModalTransitionStyleFlipHorizontal),
                       @"crossDissolve" : @(UIModalTransitionStyleCrossDissolve),
                       @"partialCurl" : @(UIModalTransitionStylePartialCurl)
                   }),
                   UIModalTransitionStyleCoverVertical, integerValue)

RCT_ENUM_CONVERTER(UIModalPresentationStyle, (@{
                       @"fullScreen" : @(UIModalPresentationFullScreen),
                       @"pageSheet" : @(UIModalPresentationPageSheet),
                       @"formSheet" : @(UIModalPresentationFormSheet),
                       @"currentContext" : @(UIModalPresentationCurrentContext),
                       @"custom" : @(UIModalPresentationCustom),
                       @"overFullScreen" : @(UIModalPresentationOverFullScreen),
                       @"overCurrentContext" : @(UIModalPresentationOverCurrentContext),
                       @"popover" : @(UIModalPresentationPopover),
                       @"none" : @(UIModalPresentationNone),
                       @"default" : @([RNNConvert defaultModalPresentationStyle])
                   }),
                   UIModalPresentationFullScreen, integerValue)

@end
