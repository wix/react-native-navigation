#import "RNNAnimationsOptions.h"

@implementation RNNAnimationsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	self.push = [[RNNScreenTransition alloc] initWithDict:dict[@"push"]];
	self.pop = [[RNNScreenTransition alloc] initWithDict:dict[@"pop"]];
	self.showModal = [[TransitionOptions alloc] initWithDict:dict[@"showModal"]];
	self.dismissModal = [[TransitionOptions alloc] initWithDict:dict[@"dismissModal"]];
	self.setStackRoot = [[RNNScreenTransition alloc] initWithDict:dict[@"setStackRoot"]];
	self.setRoot = [[RNNScreenTransition alloc] initWithDict:dict[@"setRoot"]];

	return self;
}

@end
