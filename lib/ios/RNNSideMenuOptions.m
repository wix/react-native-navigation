#import "RNNSideMenuOptions.h"
#import "RNNSideMenuController.h"

@implementation RNNSideMenuOptions

- (void)applyOn:(UIViewController *)viewController {
	RNNSideMenuController* sideMenuController = (RNNSideMenuController*)UIApplication.sharedApplication.delegate.window.rootViewController;
	if ([sideMenuController isKindOfClass:[RNNSideMenuController class]]) {
		if (self.leftSideVisible) {
			if (self.leftSideVisible.boolValue) {
				[sideMenuController showSideMenu:MMDrawerSideLeft animated:YES];
			} else {
				[sideMenuController hideSideMenu:MMDrawerSideLeft animated:YES];
			}
		}
		
		if (self.rightSideVisible) {
			if (self.rightSideVisible.boolValue) {
				[sideMenuController showSideMenu:MMDrawerSideRight animated:YES];
			} else {
				[sideMenuController hideSideMenu:MMDrawerSideRight animated:YES];
			}
		}
		
		[self resetOptions];
	}
}

-(void)mergeWith:(NSDictionary *)otherOptions {
	self.leftSideVisible = [[otherOptions valueForKey:@"left"] valueForKey:@"visible"];
	self.rightSideVisible = [[otherOptions valueForKey:@"right"] valueForKey:@"visible"];
}

-(void)resetOptions {
	self.leftSideVisible = nil;
	self.rightSideVisible = nil;
}

@end
