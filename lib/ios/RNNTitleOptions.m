#import "RNNTitleOptions.h"

@implementation RNNTitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.text = [TextParser parse:dict key:@"text"];
	self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.color = [ColorParser parse:dict key:@"color"];
	
	self.component = [[RNNComponentOptions alloc] initWithDict:dict[@"component"]];
	
	return self;
}

@end
