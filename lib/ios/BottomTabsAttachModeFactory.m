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
	BottomTabsAttachMode attachMode = [RCTConvert BottomTabsAttachMode:[[options withDefault:_defaultOptions].bottomTabs.tabsAttachMode getWithDefaultValue:@"together"]];
	switch (attachMode) {
        case BottomTabsAttachModeTogether: {
            return [BottomTabsTogetherAttacher new];
        }
            break;
        case BottomTabsAttachModeAfterInitialTab: {
            return [BottomTabsAfterInitialTabAttacher new];
        }
            break;
        case BottomTabsAttachModeOnSwitchToTab: {
            return [BottomTabsOnSwitchToTabAttacher new];
        }
        break;
        default:
            return [BottomTabsTogetherAttacher new];
            break;
    }
}

@end
