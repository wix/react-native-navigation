#import "RNNDefaultOptionsHelper.h"

@implementation RNNDefaultOptionsHelper

+ (void)recrusivelySetDefaultOptions:(RNNNavigationOptions *)defaultOptions onRootViewController:(UIViewController *)rootViewController {
	if ([rootViewController conformsToProtocol:@protocol(RNNLayoutProtocol)])
		[((UIViewController<RNNLayoutProtocol> *)rootViewController).presenter setDefaultOptions:defaultOptions];
	
	for (UIViewController<RNNLayoutProtocol>* childViewController in rootViewController.childViewControllers) {
		if ([childViewController conformsToProtocol:@protocol(RNNLayoutProtocol)]) {
			[childViewController.presenter setDefaultOptions:defaultOptions];
		}
		
		[self recrusivelySetDefaultOptions:defaultOptions onRootViewController:childViewController];
	}
}

@end
