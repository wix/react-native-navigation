#import "RNNScreenTransition.h"

@implementation RNNScreenTransition

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];

	self.topBar = dict[@"topBar"] ? [[RNNTransitionStateHolder alloc] initWithDict:dict[@"topBar"]] : nil;
	self.content = dict[@"content"] ? [[RNNTransitionStateHolder alloc] initWithDict:dict[@"content"]] : nil;
	self.bottomTabs = dict[@"bottomTabs"] ? [[RNNTransitionStateHolder alloc] initWithDict:dict[@"bottomTabs"]] : nil;
	self.enable = [BoolParser parse:dict key:@"enabled"];
	self.waitForRender = [BoolParser parse:dict key:@"waitForRender"];

	return self;
}

- (BOOL)hasCustomAnimation {
	return (self.topBar || self.content || self.bottomTabs);
}

@end
