#import "Text.h"

typedef NS_ENUM(NSInteger, AttachMode) {
    BottomTabsAttachModeTogether = 0,
    BottomTabsAttachModeAfterInitialTab,
    BottomTabsAttachModeOnSwitchToTab
};

@interface BottomTabsAttachMode : Text

- (AttachMode)get;

- (AttachMode)getWithDefaultValue:(id)defaultValue;

@end
