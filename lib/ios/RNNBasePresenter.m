#import "RNNBasePresenter.h"

@implementation RNNBasePresenter

- (instancetype)initWithOptions:(RNNNavigationOptions *)options {
	self = [super init];
	if (self) {
		self.options = options;
	}
	return self;
}

- (void)presentOn:(UIViewController *)viewController {
	[self.options applyOn:viewController];
}

- (void)present:(RNNNavigationOptions *)options on:(UIViewController *)viewController {
	
}

- (RNNNavigationOptions *)presentWithChildOptions:(RNNNavigationOptions *)childOptions on:(UIViewController *)viewController {
	RNNNavigationOptions* options = [self.options combineWithOptions:childOptions];
	[self present:options on:viewController];
	
	return options;
}

- (void)presentOnLoad:(UIViewController *)viewController {
	
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[_options mergeOptions:options overrideOptions:YES];
}

@end
