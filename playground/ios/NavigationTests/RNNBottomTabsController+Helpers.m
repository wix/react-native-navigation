#import "RNNBottomTabsController+Helpers.h"
#import "BottomTabsPresenterCreator.h"
#import "BottomTabPresenterCreator.h"

@implementation RNNBottomTabsController (Helpers)

+ (RNNBottomTabsController *)create {
	RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	return [self createWithChildren:nil];
}

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children {
	RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	return [[RNNBottomTabsController alloc] initWithLayoutInfo:nil creator:nil options:[[RNNNavigationOptions alloc] initEmptyOptions] defaultOptions:defaultOptions presenter:[BottomTabsPresenterCreator createWithDefaultOptions:defaultOptions] bottomTabPresenter:[BottomTabPresenterCreator createWithDefaultOptions:defaultOptions] dotIndicatorPresenter:[[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:defaultOptions] eventEmitter:nil childViewControllers:children bottomTabsAttacher:nil];
}

@end
