#import "RNNDefaultOptionsHelper.h"

@implementation RNNDefaultOptionsHelper

+ (void)recrusivelySetDefaultOptions:(RNNNavigationOptions *)defaultOptions onRootViewController:(UIViewController *)rootViewController {
	if ([rootViewController conformsToProtocol:@protocol(RNNLayoutProtocol)]) {
		[((UIViewController<RNNLayoutProtocol> *)rootViewController) setDefaultOptions:defaultOptions];
	}
	
	for (UIViewController<RNNLayoutProtocol>* childViewController in rootViewController.childViewControllers) {
		[self recrusivelySetDefaultOptions:defaultOptions onRootViewController:childViewController];
	}
}

@end
