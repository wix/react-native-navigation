#import "RNNTabBarPresenter.h"

@implementation RNNTabBarPresenter

- (void)presentOn:(UITabBarController *)tabBarController {
	[self.options applyOnTabBarController:tabBarController];
}

@end
