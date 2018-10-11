#import "RNNLargeTitleOptions.h"

@implementation RNNLargeTitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.visible = [BoolParser parse:dict key:@"visible"];
	self.color = [NumberParser parse:dict key:@"color"];
	self.fontFamily = [StringParser parse:dict key:@"fontFamily"];
	
	return self;
}

@end
