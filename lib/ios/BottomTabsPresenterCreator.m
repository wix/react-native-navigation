#import "BottomTabsPresenterCreator.h"
#import "BottomTabsAppearancePresenter.h"

@implementation BottomTabsPresenterCreator

+ (BottomTabsBasePresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	if (@available(iOS 13.0, *)) {
		return [[BottomTabsAppearancePresenter alloc] initWithDefaultOptions:defaultOptions];
	} else {
		return [[RNNBottomTabsPresenter alloc] initWithDefaultOptions:defaultOptions];
	}
}

@end
