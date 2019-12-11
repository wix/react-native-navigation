#import "BottomTabsAttachMode.h"
#import <React/RCTConvert.h>

@implementation BottomTabsAttachMode

- (AttachMode)get {
    return [self.class AttachMode:[super get]];
}

- (AttachMode)getWithDefaultValue:(id)defaultValue {
    return [self.class AttachMode:[super getWithDefaultValue:defaultValue]];
}

RCT_ENUM_CONVERTER(AttachMode,
(@{@"together": @(BottomTabsAttachModeTogether),
   @"afterInitialTab": @(BottomTabsAttachModeAfterInitialTab),
   @"onSwitchToTab": @(BottomTabsAttachModeOnSwitchToTab)
}), BottomTabsAttachModeTogether, integerValue)


@end
