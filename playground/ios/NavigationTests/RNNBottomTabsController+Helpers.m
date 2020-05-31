#import "RNNBottomTabsController+Helpers.h"
#import "BottomTabsPresenterCreator.h"
#import "BottomTabPresenterCreator.h"

@implementation RNNBottomTabsController (Helpers)

+ (RNNBottomTabsController *)create {
	return [self createWithChildren:nil];
}

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children {
	return [self createWithChildren:children options:[[RNNNavigationOptions alloc] initEmptyOptions]];
}

+ (RNNBottomTabsController *)createWithChildren:(NSArray *)children options:(RNNNavigationOptions *)options {
	RNNNavigationOptions* defaultOptions = [[RNNNavigationOptions alloc] initEmptyOptions];
	return [[RNNBottomTabsController alloc] initWithLayoutInfo:nil creator:nil options:options defaultOptions:defaultOptions presenter:[BottomTabsPresenterCreator createWithDefaultOptions:defaultOptions] bottomTabPresenter:[BottomTabPresenterCreator createWithDefaultOptions:defaultOptions] dotIndicatorPresenter:[[RNNDotIndicatorPresenter alloc] initWithDefaultOptions:defaultOptions] eventEmitter:nil childViewControllers:children bottomTabsAttacher:nil];
}

@end
