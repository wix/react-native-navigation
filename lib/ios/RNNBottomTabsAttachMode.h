#import "RNNEnum.h"

typedef NS_ENUM(NSInteger, AttachMode) {
    RNNBottomTabsAttachModeTogether = 0,
    RNNBottomTabsAttachModeAfterInitialTab,
    RNNBottomTabsAttachModeOnSwitchToTab
};

@interface RNNBottomTabsAttachMode : RNNEnum

- (AttachMode)get;

- (AttachMode)withDefault:(id)defaultValue;

@end
