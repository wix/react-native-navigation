
#import "RNNNavigationController.h"

@implementation RNNNavigationController

- (UIInterfaceOrientationMask)supportedInterfaceOrientations {
	return self.viewControllers.lastObject.supportedInterfaceOrientations;
}

- (BOOL)isCustomTransitioned {
	return NO;
}

- (BOOL)isAnimated {
	UIViewController<RNNRootViewProtocol>* rootVC = (UIViewController<RNNRootViewProtocol>*) self.topViewController;
	return rootVC.isAnimated;
}

- (void)setOptions:(RNNNavigationOptions *)options {
	((UIViewController<RNNRootViewProtocol>*)self.topViewController).options = options;
}

- (NSString *)componentId {
	return ((UIViewController<RNNRootViewProtocol>*)self.topViewController).componentId;
}

@end
