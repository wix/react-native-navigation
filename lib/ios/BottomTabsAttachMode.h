#import "RNNEnum.h"

typedef NS_ENUM(NSInteger, AttachMode) {
    BottomTabsAttachModeTogether = 0,
    BottomTabsAttachModeAfterInitialTab,
    BottomTabsAttachModeOnSwitchToTab
};

@interface BottomTabsAttachMode : RNNEnum

- (AttachMode)get;

- (AttachMode)withDefault:(id)defaultValue;

@end
