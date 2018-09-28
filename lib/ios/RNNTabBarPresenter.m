#import "RNNTabBarPresenter.h"

@implementation RNNTabBarPresenter

- (void)presentOn:(UITabBarController *)tabBarController {
	[self.options applyOnTabBarController:tabBarController];
}

- (void)present:(RNNNavigationOptions *)options on:(UITabBarController *)tabBarController {
	[self.options applyOnTabBarController:tabBarController];
}

@end
