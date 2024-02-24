#import "RNNBottomTabsAttachModeFactory.h"
#import "RNNBottomTabsAfterInitialTabAttacher.h"
#import "RNNBottomTabsOnSwitchToTabAttacher.h"
#import "RNNBottomTabsTogetherAttacher.h"

@implementation RNNBottomTabsAttachModeFactory

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    self = [super init];
    _defaultOptions = defaultOptions;
    return self;
}

- (RNNBottomTabsBaseAttacher *)fromOptions:(RNNNavigationOptions *)options {
    AttachMode attachMode =
        [[options withDefault:_defaultOptions].bottomTabs.tabsAttachMode withDefault:@"together"];
    switch (attachMode) {
    case BottomTabsAttachModeAfterInitialTab: {
        return [RNNBottomTabsAfterInitialTabAttacher new];
    }
    case BottomTabsAttachModeOnSwitchToTab: {
        return [RNNBottomTabsOnSwitchToTabAttacher new];
    }
    default:
        return [RNNBottomTabsTogetherAttacher new];
        break;
    }
}

@end
