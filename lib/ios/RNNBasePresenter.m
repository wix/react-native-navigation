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

- (void)applyOptions:(RNNNavigationOptions *)options {
	[self.bottomTabPresenter applyOptions:options];
}

- (void)mergeOptions:(RNNNavigationOptions *)options {
	
}

@end
