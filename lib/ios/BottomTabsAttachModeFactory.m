#import "BottomTabsAttachModeFactory.h"
#import "BottomTabsTogetherAttacher.h"
#import "BottomTabsOnSwitchToTabAttacher.h"
#import "BottomTabsAfterInitialTabAttacher.h"

typedef NS_ENUM(NSInteger, BottomTabsAttachMode) {
    BottomTabsAttachModeTogether = 0,
    BottomTabsAttachModeAfterInitialTab,
    BottomTabsAttachModeOnSwitchToTab
};

@implementation BottomTabsAttachModeFactory

- (instancetype)initWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	self = [super init];
	_defaultOptions = defaultOptions;
	return self;
}

- (BottomTabsBaseAttacher *)fromOptions:(RNNNavigationOptions *)options {
	BottomTabsAttachMode attachMode = [self.class BottomTabsAttachMode:[[options withDefault:_defaultOptions].bottomTabs.tabsAttachMode getWithDefaultValue:@"together"]];
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

RCT_ENUM_CONVERTER(BottomTabsAttachMode,
(@{@"together": @(BottomTabsAttachModeTogether),
   @"afterInitialTab": @(BottomTabsAttachModeAfterInitialTab),
   @"onSwitchToTab": @(BottomTabsAttachModeOnSwitchToTab)
   }), BottomTabsAttachModeTogether, integerValue)

@end
