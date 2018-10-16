#import "RNNBasePresenter.h"
#import "RNNBottomTabPresenter.h"

@interface RNNBasePresenter()

@property (nonatomic, strong) RNNBottomTabPresenter* bottomTabPresenter;

@end

@implementation RNNBasePresenter

- (void)bindViewController:(UIViewController *)bindedViewController {
	_bindedViewController = bindedViewController;
	self.bottomTabPresenter = [[RNNBottomTabPresenter alloc] initWithViewController:bindedViewController];
}

- (void)applyOptionsOnWillMoveToParentViewController:(RNNNavigationOptions *)options {
	[self.bottomTabPresenter applyOptions:options];
}

- (void)applyOptions:(RNNNavigationOptions *)initialOptions {
	[self.bottomTabPresenter applyOptions:initialOptions];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	
}

- (void)setDefaultOptions:(RNNNavigationOptions *)defaultOptions {
	_defaultOptions = defaultOptions;
	[self.bottomTabPresenter setDefaultOptions:defaultOptions];
}

@end
