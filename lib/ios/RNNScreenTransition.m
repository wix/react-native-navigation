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

- (NSTimeInterval)maxDuration {
	NSTimeInterval maxDuration = 0;
	if ([self.topBar maxDuration] > maxDuration) {
		maxDuration = [self.topBar maxDuration];
	}
    
	if ([self.content maxDuration] > maxDuration) {
		maxDuration = [self.content maxDuration];
	}
    
	if ([self.bottomTabs maxDuration] > maxDuration) {
		maxDuration = [self.bottomTabs maxDuration];
	}
    
    for (ElementTransitionOptions* elementTransition in self.elementTransitions) {
        if (elementTransition.maxDuration > maxDuration) {
            maxDuration = elementTransition.maxDuration;
        }
    }
    
    for (SharedElementTransitionOptions* sharedElementTransition in self.sharedElementTransitions) {
        if (sharedElementTransition.maxDuration > maxDuration) {
            maxDuration = sharedElementTransition.maxDuration;
        }
    }
	
	return maxDuration;
}

@end
