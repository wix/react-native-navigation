#import "RNNStatusBarOptions.h"
#import "UIViewController+RNNOptions.h"

@implementation RNNStatusBarOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.blur = [BoolParser parse:dict key:@"blur"];
	self.hideWithTopBar = [BoolParser parse:dict key:@"hideWithTopBar"];
	self.visible = [BoolParser parse:dict key:@"visible"];
	self.animate = [BoolParser parse:dict key:@"animate"];
	self.style = [TextParser parse:dict key:@"style"];
	
	return self;
}

@end
