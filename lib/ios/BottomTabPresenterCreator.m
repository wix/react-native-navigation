#import "BottomTabPresenterCreator.h"
#import "BottomTabAppearancePresenter.h"

@implementation BottomTabPresenterCreator

+ (BottomTabPresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	if (@available(iOS 13.0, *)) {
		return [[BottomTabAppearancePresenter alloc] initWithDefaultOptions:defaultOptions];
	} else {
		return [[BottomTabPresenter alloc] initWithDefaultOptions:defaultOptions];
	}
}

@end
