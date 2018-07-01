#import "RNNSideMenuOptions.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuOptions

- (void)applyOn:(UIViewController *)viewController {
	[self.left applyOnSide:MMDrawerSideLeft viewController:viewController];
	[self.right applyOnSide:MMDrawerSideRight viewController:viewController];
}

@end
