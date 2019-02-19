#import <React/RCTConvert.h>

@interface RCTConvert (Modal)

@end

@implementation RCTConvert (Modal)

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

RCT_ENUM_CONVERTER(UIModalPresentationStyle,
				   (@{@"fullScreen": @(UIModalPresentationFullScreen),
					  @"pageSheet": @(UIModalPresentationPageSheet),
					  @"formSheet": @(UIModalPresentationFormSheet),
					  @"currentContext": @(UIModalPresentationCurrentContext),
					  @"custom": @(UIModalPresentationCustom),
					  @"overFullScreen": @(UIModalPresentationOverFullScreen),
					  @"overCurrentContext": @(UIModalPresentationOverCurrentContext),
					  @"popover": @(UIModalPresentationPopover),
					  @"none": @(UIModalPresentationNone)
					  }), UIModalPresentationFullScreen, integerValue)

#endif


@end

