#import "RNNBottomTabsPresenterCreator.h"
#import "RNNBottomTabsAppearancePresenter.h"

@implementation RNNBottomTabsPresenterCreator

+ (RNNBottomTabsBasePresenter *)createWithDefaultOptions:(RNNNavigationOptions *)defaultOptions {
    if (@available(iOS 13.0, *)) {
        return [[RNNBottomTabsAppearancePresenter alloc] initWithDefaultOptions:defaultOptions];
    } else {
        return [[RNNBottomTabsPresenter alloc] initWithDefaultOptions:defaultOptions];
    }
}

@end
