#import "RNNBackgroundOptions.h"


@implementation RNNBackgroundOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.color = [ColorParser parse:dict key:@"color"];
	self.translucent = [BoolParser parse:dict key:@"translucent"];
	self.blur = [BoolParser parse:dict key:@"blur"];
	self.clipToBounds = [BoolParser parse:dict key:@"clipToBounds"];
	self.component = [[RNNComponentOptions alloc] initWithDict:dict[@"component"]];
	
	return self;
}

@end
