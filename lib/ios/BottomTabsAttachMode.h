#import "Enum.h"

typedef NS_ENUM(NSInteger, AttachMode) {
    BottomTabsAttachModeTogether = 0,
    BottomTabsAttachModeAfterInitialTab,
    BottomTabsAttachModeOnSwitchToTab
};

@interface BottomTabsAttachMode : Enum

- (AttachMode)get;

- (AttachMode)getWithDefaultValue:(id)defaultValue;

@end
