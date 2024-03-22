#import "RNNBottomTabsAttachMode.h"
#import <React/RCTConvert.h>

@implementation RNNBottomTabsAttachMode

- (AttachMode)convertString:(NSString *)string {
    return [self.class AttachMode:string];
}

RCT_ENUM_CONVERTER(AttachMode, (@{
   @"together" : @(RNNBottomTabsAttachModeTogether),
   @"afterInitialTab" : @(RNNBottomTabsAttachModeAfterInitialTab),
   @"onSwitchToTab" : @(RNNBottomTabsAttachModeOnSwitchToTab)
}), RNNBottomTabsAttachModeTogether, integerValue)

@end
