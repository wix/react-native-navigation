#import "RNNSubtitleOptions.h"

@implementation RNNSubtitleOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.text = [StringParser parse:dict key:@"text"];
	self.alignment = [StringParser parse:dict key:@"alignment"];
	self.fontFamily = [StringParser parse:dict key:@"fontFamily"];
	self.fontSize = [NumberParser parse:dict key:@"fontSize"];
	self.color = [NumberParser parse:dict key:@"color"];
	
	return self;
}

@end
