#import "BottomTabsAttachModeFactory.h"
#import "BottomTabsTogetherAttacher.h"
#import "BottomTabsOnSwitchToTabAttacher.h"
#import "BottomTabsAfterInitialTabAttacher.h"

@implementation BottomTabsAttachModeFactory

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super init];
	_defaultOptions = defaultOptions;
	return self;
}

- (BottomTabsBaseAttacher *)fromOptions:(RNNNavigationOptions *)options {
    AttachMode attachMode = [[options withDefault:_defaultOptions].bottomTabs.tabsAttachMode getWithDefaultValue:@"together"];
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
