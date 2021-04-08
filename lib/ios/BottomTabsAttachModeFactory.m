#import "BottomTabsAttachModeFactory.h"
#import "BottomTabsAfterInitialTabAttacher.h"
#import "BottomTabsOnSwitchToTabAttacher.h"
#import "BottomTabsTogetherAttacher.h"

@implementation BottomTabsAttachModeFactory

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    _defaultOptions = defaultOptions;
    return self;
}

- (BottomTabsBaseAttacher *)fromOptions:(RNNNavigationOptions *)options {
    AttachMode attachMode =
        [[options withDefault:_defaultOptions].bottomTabs.tabsAttachMode withDefault:@"together"];
    switch (attachMode) {
    case BottomTabsAttachModeAfterInitialTab: {
        return [BottomTabsAfterInitialTabAttacher new];
    }
    case BottomTabsAttachModeOnSwitchToTab: {
        return [BottomTabsOnSwitchToTabAttacher new];
    }
    default:
        return [BottomTabsTogetherAttacher new];
        break;
    }
}

@end
