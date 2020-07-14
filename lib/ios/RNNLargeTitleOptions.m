#import "RNNLargeTitleOptions.h"

@implementation RNNLargeTitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.visible = [BoolParser parse:dict key:@"visible"];
	self.color = [ColorParser parse:dict key:@"color"];
	self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
	self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
	
	return self;
}

@end
