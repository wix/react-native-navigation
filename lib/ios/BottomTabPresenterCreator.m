#import "BottomTabPresenterCreator.h"
#import "BottomTabAppearancePresenter.h"

@implementation BottomTabPresenterCreator

+ (BottomTabPresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions children:(NSArray<UIViewController *> *)children {
	if (@available(iOS 13.0, *)) {
		return [[BottomTabAppearancePresenter alloc] initWithDefaultOptions:defaultOptions children:children];
	} else {
		return [[BottomTabPresenter alloc] initWithDefaultOptions:defaultOptions];
	}
}

@end
