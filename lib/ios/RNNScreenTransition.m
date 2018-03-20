#import "RNNScreenTransition.h"

@implementation RNNScreenTransition

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	self.topBar = [[RNNTransitionStateHolder alloc] initWithTransition:dict[@"topBar"]];
	self.content = [[RNNTransitionStateHolder alloc] initWithTransition:dict[@"content"]];
	self.bottomTabs = [[RNNTransitionStateHolder alloc] initWithTransition:dict[@"bottomTabs"]];
	
	return self;
}

@end
