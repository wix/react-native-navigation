#import "RNNBackgroundOptions.h"

@implementation RNNBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super initWithDict:dict];
	self.component = [[RNNComponentOptions alloc] initWithDict:dict[@"component"]];
	
	return self;
}

- (void)applyOn:(UIViewController *)viewController {
	if (self.color) {
		UIColor* backgroundColor = [RCTConvert UIColor:self.color];
		viewController.navigationController.navigationBar.barTintColor = backgroundColor;
	} else {
		viewController.navigationController.navigationBar.barTintColor = nil;
	}
}

@end
