#import "RNNScreenTransition.h"
#import "OptionsArrayParser.h"

@implementation RNNScreenTransition

- (instancetype)initWithDict:(NSDictionary *)dict {
	self = [super init];

	self.topBar = [[ElementTransitionOptions alloc] initWithDict:dict[@"topBar"]];
	self.content = [[ElementTransitionOptions alloc] initWithDict:dict[@"content"]];
	self.bottomTabs = [[ElementTransitionOptions alloc] initWithDict:dict[@"bottomTabs"]];
	self.enable = [BoolParser parse:dict key:@"enabled"];
	self.waitForRender = [BoolParser parse:dict key:@"waitForRender"];
    self.duration = [TimeIntervalParser parse:dict key:@"duration"];
    self.sharedElementTransitions = [OptionsArrayParser parse:dict key:@"sharedElementTransitions" ofClass:SharedElementTransitionOptions.class];
	self.elementTransitions = [OptionsArrayParser parse:dict key:@"elementTransitions" ofClass:ElementTransitionOptions.class];
    
	return self;
}

- (BOOL)hasCustomAnimation {
	return (self.topBar.hasAnimation || self.content.hasAnimation || self.bottomTabs.hasAnimation || self.sharedElementTransitions || self.elementTransitions);
}

- (BOOL)shouldWaitForRender {
    return [self.waitForRender getWithDefaultValue:NO] || self.hasCustomAnimation;
}

- (NSTimeInterval)minDuration {
	NSTimeInterval minDuration = 0;
	if ([self.topBar minDuration] > 0) {
		minDuration = [self.topBar minDuration];
	}
	if ([self.content minDuration] > 0) {
		minDuration = [self.content minDuration];
	}
	if ([self.bottomTabs minDuration] > 0) {
		minDuration = [self.bottomTabs minDuration];
	}
	
	return minDuration;
}

@end
