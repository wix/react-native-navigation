#import "RNNComponentOptions.h"

@implementation RNNComponentOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.name = [TextParser parse:dict key:@"name"];
	self.componentId = [TextParser parse:dict key:@"componentId"];
	self.alignment = [TextParser parse:dict key:@"alignment"];
	
	return self;
}

@end
