#import "RNNSubtitleOptions.h"

@implementation RNNSubtitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.text = [TextParser parse:dict key:@"text"];
	self.alignment = [TextParser parse:dict key:@"alignment"];
	self.fontFamily = [TextParser parse:dict key:@"fontFamily"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.fontWeight = [TextParser parse:dict key:@"fontWeight"];
	self.color = [ColorParser parse:dict key:@"color"];
	
	return self;
}

@end
