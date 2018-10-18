#import "RNNOverlayOptions.h"
#import <React/RCTRootView.h>

@implementation RNNOverlayOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.interceptTouchOutside = [BoolParser parse:dict key:@"interceptTouchOutside"];
	
	return self;
}

- (void)applyOn:(UIViewController *)viewController {
//	if (self.interceptTouchOutside) {
//		RCTRootView* rootView = (RCTRootView*)viewController.view;
//		rootView.passThroughTouches = ![self.interceptTouchOutside boolValue];
//	}
}

@end
