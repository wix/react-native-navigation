#import "RNNTransitionsOptions.h"

@implementation RNNTransitionsOptions

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];
	
	[self mergeWith:dict];
	
	return self;
}

- (void)mergeWith:(NSDictionary *)otherOptions {
	self.push = otherOptions[@"push"] ? [[RNNScreenTransition alloc] initWithDict:otherOptions[@"push"]] : _push;
	self.pop = otherOptions[@"pop"] ? [[RNNScreenTransition alloc] initWithDict:otherOptions[@"pop"]] : _pop;
	self.showModal = otherOptions[@"showModal"] ? [[RNNScreenTransition alloc] initWithDict:otherOptions[@"showModal"]] : _showModal;
	self.dismissModal = otherOptions[@"dismissModal"] ? [[RNNScreenTransition alloc] initWithDict:otherOptions[@"dismissModal"]] : _dismissModal;
}

@end
