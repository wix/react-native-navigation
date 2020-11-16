#import "BottomTabsAttachMode.h"
#import <React/RCTConvert.h>

@implementation BottomTabsAttachMode

- (AttachMode)convertString:(NSString *)string {
    return [self.class AttachMode:string];
}

RCT_ENUM_CONVERTER(AttachMode, (@{
                       @"together" : @(BottomTabsAttachModeTogether),
                       @"afterInitialTab" : @(BottomTabsAttachModeAfterInitialTab),
                       @"onSwitchToTab" : @(BottomTabsAttachModeOnSwitchToTab)
                   }),
                   BottomTabsAttachModeTogether, integerValue)

@end
