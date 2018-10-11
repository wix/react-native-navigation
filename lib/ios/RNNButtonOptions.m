#import "RNNButtonOptions.h"

@implementation RNNButtonOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.fontFamily = [StringParser parse:dict key:@"fontFamily"];
	self.text = [StringParser parse:dict key:@"text"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.color = [NumberParser parse:dict key:@"color"];
	self.disabledColor = [NumberParser parse:dict key:@"disabledColor"];
	self.icon = [NumberParser parse:dict key:@"icon"];
	self.enabled = [BoolParser parse:dict key:@"enabled"];
	

	return self;
}

@end
