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
	
}

- (void)presentOnLoad:(UIViewController *)viewController {
	
}

- (void)applyOptions:(RNNNavigationOptions *)options {
	[_options mergeOptions:options overrideOptions:NO];
}

- (void)overrideOptions:(RNNNavigationOptions *)options {
	[_options mergeOptions:options overrideOptions:YES];
}

@end
