#import "RNNTabBarPresenter.h"

@implementation RNNTabBarPresenter

- (void)present:(RNNNavigationOptions *)options onViewControllerDidLoad:(UITabBarController *)tabBarController {
	[options applyOnTabBarController:tabBarController];
}

@end
