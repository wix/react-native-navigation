#import "RNNScreenTransition.h"

@implementation RNNScreenTransition

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];

	self.topBar = [[RNNElementTransitionOptions alloc] initWithDict:dict[@"topBar"]];
	self.content = [[RNNElementTransitionOptions alloc] initWithDict:dict[@"content"]];
	self.bottomTabs = [[RNNElementTransitionOptions alloc] initWithDict:dict[@"bottomTabs"]];
	self.enable = [BoolParser parse:dict key:@"enabled"];
	self.waitForRender = [BoolParser parse:dict key:@"waitForRender"];

	return self;
}

- (BOOL)hasCustomAnimation {
	return (self.topBar.hasAnimation || self.content.hasAnimation || self.bottomTabs.hasAnimation);
}

- (double)maxDuration {
	double maxDuration = 0;
	if ([self.topBar maxDuration] > 0) {
		maxDuration = [self.topBar maxDuration];
	}
	if ([self.content maxDuration] > 0) {
		maxDuration = [self.content maxDuration];
	}
	if ([self.bottomTabs maxDuration] > 0) {
		maxDuration = [self.bottomTabs maxDuration];
	}
	
	return maxDuration;
}

@end
