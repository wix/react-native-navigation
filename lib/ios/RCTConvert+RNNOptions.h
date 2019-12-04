#import <UIKit/UIKit.h>
#import <React/RCTConvert.h>

typedef NS_ENUM(NSInteger, BottomTabsAttachMode) {
    BottomTabsAttachModeTogether = 0,
    BottomTabsAttachModeAfterInitialTab,
    BottomTabsAttachModeOnSwitchToTab
};

@interface RCTConvert (RNNOptions)

@end

@implementation RCTConvert (RNNOptions)

RCT_ENUM_CONVERTER(BottomTabsAttachMode,
(@{@"together": @(BottomTabsAttachModeTogether),
   @"afterInitialTab": @(BottomTabsAttachModeAfterInitialTab),
   @"onSwitchToTab": @(BottomTabsAttachModeOnSwitchToTab)
   }), BottomTabsAttachModeTogether, integerValue)

@end

