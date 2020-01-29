#import "RNNOverlayOptions.h"
#import <React/RCTRootView.h>

@implementation RNNOverlayOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.interceptTouchOutside = [BoolParser parse:dict key:@"interceptTouchOutside"];
	self.handleKeyboardEvents = [BoolParser parse:dict key:@"handleKeyboardEvents"];
	
	return self;
}

@end
