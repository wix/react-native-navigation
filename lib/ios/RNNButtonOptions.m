#import "RNNButtonOptions.h"

@implementation RNNButtonOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
	self.text = [TextParser parse:dict key:@"text"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.color = [ColorParser parse:dict key:@"color"];
	self.disabledColor = [ColorParser parse:dict key:@"disabledColor"];
	self.icon = [ImageParser parse:dict key:@"icon"];
	self.iconInsets = [[RNNInsetsOptions alloc] initWithDict:dict[@"iconInsets"]];
	self.enabled = [BoolParser parse:dict key:@"enabled"];
	

	return self;
}

@end
