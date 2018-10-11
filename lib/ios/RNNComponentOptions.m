#import "RNNComponentOptions.h"

@implementation RNNComponentOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.name = [StringParser parse:dict key:@"name"];
	self.componentId = [StringParser parse:dict key:@"componentId"];
	self.alignment = [StringParser parse:dict key:@"alignment"];
	
	return self;
}

@end
