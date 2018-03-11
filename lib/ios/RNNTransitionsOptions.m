#import "RNNTransitionsOptions.h"

@implementation RNNTransitionsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.push = [[RNNScreenTransition alloc] initWithDict:dict[@"push"]];
	self.pop = [[RNNScreenTransition alloc] initWithDict:dict[@"pop"]];
	
	return self;
}

@end
