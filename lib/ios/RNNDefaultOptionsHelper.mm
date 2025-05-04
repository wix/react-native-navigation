#import "RNNDefaultOptionsHelper.h"

@implementation RNNDefaultOptionsHelper

+ (void)recursivelySetDefaultOptions:(RNNNavigationOptions *)defaultOptions
                onRootViewController:(UIViewController *)rootViewController {
    if ([rootViewController conformsToProtocol:@protocol(RNNLayoutProtocol)]) {
        [((UIViewController<RNNLayoutProtocol> *)rootViewController)
            setDefaultOptions:defaultOptions];
    }

    for (UIViewController<RNNLayoutProtocol> *childViewController in rootViewController
             .childViewControllers) {
        [self recursivelySetDefaultOptions:defaultOptions onRootViewController:childViewController];
    }
}

@end
